package cvut.fel.virtualdoctor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cvut.fel.virtualdoctor.dto.DiagnosisDTO;
import cvut.fel.virtualdoctor.dto.PatientInputDTO;
import cvut.fel.virtualdoctor.classifier.client.ClassifierClientRest;
import cvut.fel.virtualdoctor.classifier.client.ClassifierInput;
import cvut.fel.virtualdoctor.classifier.client.ClassifierOutput;
import cvut.fel.virtualdoctor.classifier.mapper.ClassifierMapper;
import cvut.fel.virtualdoctor.dto.mapper.DiagnosisMapper;
import cvut.fel.virtualdoctor.model.*;
import cvut.fel.virtualdoctor.repository.DiseaseRepository;
import cvut.fel.virtualdoctor.repository.PatientRepository;
import cvut.fel.virtualdoctor.repository.SymptomRepository;
import cvut.fel.virtualdoctor.repository.PatientInputRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
public class EvaluatorService implements IEvaluatorService {

    private static final Logger logger = LoggerFactory.getLogger(EvaluatorService.class);

    // TODO revise this --> seams like a lot of dependencies and lot of responsibility
    SymptomService symptomService;
    DiagnosisService diagnosisService;
    DiseaseRepository diseaseRepository;
    PatientRepository patientRepository;
    SymptomRepository symptomRepository;
    PatientInputRepository patientInputRepository;
    ClassifierClientRest classifierClientRest;
    ClassifierMapper classifierMapper;
    ObjectMapper objectMapper;
    DiagnosisMapper diagnosisMapper;
    VitalSignsObserver vitalSignsObserver;

    /**
     * @param patientInputDTO The name input to evaluate for diagnosis
     * @return A CompletableFuture that will contain the diagnosis once it is evaluated, since
     * the process is asynchronous.
     */
    @Async
    public CompletableFuture<DiagnosisDTO> evaluateUserInput(PatientInputDTO patientInputDTO) {

        // TODO move to service
        Patient patient = patientRepository.findByName(patientInputDTO.name())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // TODO move to service
        List<Symptom> symptoms = patientInputDTO.symptoms().stream().map(
                symptomName -> symptomRepository.findByName(symptomName)
                        .orElseThrow(() -> new RuntimeException("Symptom not found"))
        ).toList();

        VitalSigns vitalSigns = vitalSignsObserver.provideVitalSigns(patient);

        PatientInput patientInput = new PatientInput(patient, symptoms, vitalSigns);
        try {
            // TODO move to service
            patientInput = patientInputRepository.save(patientInput);
        } catch (Exception e) {
            logger.error("Failed to save name input to database: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }

        CompletableFuture<Diagnosis> diagnosis = classify(patientInput);

        diagnosis.thenAccept(d -> {
            try {
                diagnosisService.saveDiagnosis(d);
            } catch (Exception e) {
                logger.error("Error saving diagnosis: {}", e.getMessage());
            }
        });

        return diagnosis.thenApply(d -> diagnosisMapper.toDTO(d));
    }

    private CompletableFuture<Diagnosis> classify(PatientInput patientInput) {
        logger.info("Evaluating diagnosis...");
        ClassifierInput classifierInput = classifierMapper.mapUserInputToEvaluatorInput(patientInput);

        // Send request to Python Evaluator Service endpoint
        CompletableFuture<ClassifierOutput> future = classifierClientRest.getPrediction(classifierInput);

        // Wait for process to finish and return result
        return future.thenApply(response -> {
            logger.info("Response from Classifier: {}", response);
            DifferentialList differentialList = new DifferentialList(response.predictions());
            return diagnosisService.createDiagnosis(patientInput, response.version(), differentialList);
        });
    }
}

