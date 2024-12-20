package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.classifier.client.ClassifierClientRest;
import cvut.fel.virtualdoctor.classifier.client.ClassifierOutput;
import cvut.fel.virtualdoctor.model.Diagnosis;
import cvut.fel.virtualdoctor.model.PatientInput;
import cvut.fel.virtualdoctor.model.VitalSigns;
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

    SymptomService symptomService;
    DiagnosisService diagnosisService;
    PatientInputService patientInputService;
    ClassifierClientRest classifierClientRest;
    VitalSignsObserverServiceService vitalSignsObserverService;

    /**
     * @param patientInput The name input to evaluate for diagnosis
     * @return A CompletableFuture that will contain the diagnosis once it is evaluated, since
     * the process is asynchronous.
     */
    @Async
    public CompletableFuture<Diagnosis> evaluatePatientInput(PatientInput patientInput) {
        patientInput = patientInputService.save(patientInput);
        return classify(patientInput);
    }

    private CompletableFuture<Diagnosis> classify(PatientInput patientInput) {
        logger.info("Evaluating diagnosis...");
        VitalSigns vitalSigns = vitalSignsObserverService.provideVitalSigns(patientInput.getPatient());

        // Send request to Python Evaluator Service endpoint
        CompletableFuture<ClassifierOutput> future = classifierClientRest.getPrediction(patientInput, vitalSigns);

        // Wait for process to finish and return result
        return future.thenApply(response -> {
            logger.info("Response from Classifier: {}", response);
            return diagnosisService.createDiagnosis(patientInput, response);
        });
    }
}

