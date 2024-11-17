package cvut.fel.virtualdoctor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cvut.fel.virtualdoctor.dto.DiagnosisDTO;
import cvut.fel.virtualdoctor.dto.UserInputDTO;
import cvut.fel.virtualdoctor.classifier.client.ClassifierClientRest;
import cvut.fel.virtualdoctor.classifier.client.ClassifierInput;
import cvut.fel.virtualdoctor.classifier.client.ClassifierOutput;
import cvut.fel.virtualdoctor.classifier.mapper.ClassifierMapper;
import cvut.fel.virtualdoctor.dto.mapper.DiagnosisMapper;
import cvut.fel.virtualdoctor.model.*;
import cvut.fel.virtualdoctor.repository.DiseaseRepository;
import cvut.fel.virtualdoctor.repository.SymptomRepository;
import cvut.fel.virtualdoctor.repository.UserInputRepository;
import cvut.fel.virtualdoctor.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
public class EvaluatorService implements IEvaluatorService {

    private static final Logger logger = LoggerFactory.getLogger(EvaluatorService.class);

    SymptomService symptomService;
    DiagnosisService diagnosisService;
    DiseaseRepository diseaseRepository;
    UserRepository userRepository;
    SymptomRepository symptomRepository;
    UserInputRepository userInputRepository;
    ClassifierClientRest classifierClientRest;
    ClassifierMapper classifierMapper;
    ObjectMapper objectMapper;
    DiagnosisMapper diagnosisMapper;

    /**
     * @param userInputDTO The user input to evaluate for diagnosis
     * @return A CompletableFuture that will contain the diagnosis once it is evaluated, since
     * the process is asynchronous.
     */
    @Async
    public CompletableFuture<DiagnosisDTO> evaluateUserInput(UserInputDTO userInputDTO) {

        User user = userRepository.findByUsername(userInputDTO.user())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Symptom> symptoms = userInputDTO.symptoms().stream().map(
                symptomName -> symptomRepository.findByName(symptomName)
                        .orElseThrow(() -> new RuntimeException("Symptom not found"))
        ).toList();

        // TODO: get vital signs for the user from specific service --> In question where should
        //  the source of this data.
        VitalSigns vitalSigns = new VitalSigns(
                LocalDateTime.now(),
                36.6,
                120,
                25,
                37.5
        );

        UserInput userInput = new UserInput(user, symptoms, vitalSigns);
        try {
            userInput = userInputRepository.save(userInput);
        } catch (Exception e) {
            logger.error("Failed to save user input to database: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }

        CompletableFuture<Diagnosis> diagnosis = classify(userInput);

        diagnosis.thenAccept(d -> {
            try {
                diagnosisService.saveDiagnosis(d);
            } catch (Exception e) {
                logger.error("Error saving diagnosis: {}", e.getMessage());
            }
        });

        return diagnosis.thenApply(d -> diagnosisMapper.toDTO(d));
    }

    private CompletableFuture<Diagnosis> classify(UserInput userInput) {
        logger.info("Evaluating diagnosis...");
        ClassifierInput classifierInput = classifierMapper.mapUserInputToEvaluatorInput(userInput);

        // Send request to Python Evaluator Service endpoint
        CompletableFuture<ClassifierOutput> future = classifierClientRest.getPrediction(classifierInput);

        // Wait for process to finish and return result
        return future.thenApply(response -> {
            logger.info("Response from Classifier: {}", response);
            DifferentialList differentialList = new DifferentialList(response.predictions());
            return diagnosisService.createDiagnosis(response.version(), differentialList);
        });
    }
}

