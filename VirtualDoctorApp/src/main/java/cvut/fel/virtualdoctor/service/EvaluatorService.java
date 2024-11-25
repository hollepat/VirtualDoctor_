package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.classifier.client.ClassifierClientRest;
import cvut.fel.virtualdoctor.classifier.client.ClassifierInput;
import cvut.fel.virtualdoctor.classifier.client.ClassifierOutput;
import cvut.fel.virtualdoctor.classifier.mapper.ClassifierMapper;
import cvut.fel.virtualdoctor.model.Diagnosis;
import cvut.fel.virtualdoctor.model.DifferentialList;
import cvut.fel.virtualdoctor.model.PatientInput;
import cvut.fel.virtualdoctor.model.VitalSigns;
import cvut.fel.virtualdoctor.repository.PatientInputRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
public class EvaluatorService implements IEvaluatorService {

    private static final Logger logger = LoggerFactory.getLogger(EvaluatorService.class);

    // TODO revise this --> seams like a lot of dependencies and lot of responsibility
    SymptomService symptomService;
    DiagnosisService diagnosisService;
    PatientInputRepository patientInputRepository;
    ClassifierClientRest classifierClientRest;
    ClassifierMapper classifierMapper;
    VitalSignsObserver vitalSignsObserver;

    /**
     * @param patientInput The name input to evaluate for diagnosis
     * @return A CompletableFuture that will contain the diagnosis once it is evaluated, since
     * the process is asynchronous.
     */
    @Async
    public CompletableFuture<Diagnosis> evaluateUserInput(PatientInput patientInput) {

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

        return diagnosis;
    }

    private CompletableFuture<Diagnosis> classify(PatientInput patientInput) {
        logger.info("Evaluating diagnosis...");
        VitalSigns vitalSigns = vitalSignsObserver.provideVitalSigns(patientInput.getPatient());

        ClassifierInput classifierInput = classifierMapper.mapUserInputToEvaluatorInput(patientInput, vitalSigns);

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

