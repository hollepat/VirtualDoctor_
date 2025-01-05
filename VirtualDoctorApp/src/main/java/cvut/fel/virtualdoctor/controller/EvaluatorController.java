package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.DiagnosisDTO;
import cvut.fel.virtualdoctor.dto.PatientInputDTO;
import cvut.fel.virtualdoctor.dto.mapper.DiagnosisMapper;
import cvut.fel.virtualdoctor.model.Patient;
import cvut.fel.virtualdoctor.model.PatientInput;
import cvut.fel.virtualdoctor.model.Symptom;
import cvut.fel.virtualdoctor.service.EvaluatorService;
import cvut.fel.virtualdoctor.service.PatientService;
import cvut.fel.virtualdoctor.service.SymptomService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@RestController()
@RequestMapping("api/evaluation")
@AllArgsConstructor
public class EvaluatorController implements IEvaluatorController {

    private static final Logger logger = LoggerFactory.getLogger(EvaluatorController.class);
    private final EvaluatorService evaluatorService;
    private final PatientService patientService;
    private final SymptomService symptomService;
    private final DiagnosisMapper diagnosisMapper;

    @PostMapping("/evaluate")
    public CompletableFuture<ResponseEntity<DiagnosisDTO>> evaluateDiagnosis(@RequestBody PatientInputDTO patientInputDTO) {
        logger.info("Received request to evaluate diagnosis");

        if (patientInputDTO.symptoms().isEmpty()) {
            logger.warn("No symptoms provided, returning bad request");
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }

        // Validate symptoms
        List<String> invalidSymptoms = new ArrayList<>();
        List<Symptom> symptoms = patientInputDTO.symptoms().stream()
                .map(symptomName -> {
                    Symptom symptom = symptomService.findByName(symptomName);
                    if (symptom == null) {
                        logger.warn("Symptom not found: {}", symptomName);
                        invalidSymptoms.add(symptomName);
                    }
                    return symptom;
                })
                .filter(Objects::nonNull) // Filter out null symptoms
                .toList();

        // Check if there are invalid symptoms
        if (!invalidSymptoms.isEmpty()) {
            logger.info("Invalid symptoms found: {}", invalidSymptoms);
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }


        try {
            Patient patient = patientService.findByName(patientInputDTO.name());
            PatientInput patientInput = new PatientInput(patient, symptoms, patientInputDTO.cholesterolLevel());

            // Evaluate diagnosis asynchronously
            return evaluatorService.evaluatePatientInput(patientInput)
                    .thenApply(diagnosis -> {
                        DiagnosisDTO diagnosisDTO = diagnosisMapper.toDTO(diagnosis);
                        logger.info("Diagnosis sent: {}", diagnosisDTO.toString());
                        return ResponseEntity.ok(diagnosisDTO);
                    })
                    .exceptionally(ex -> {
                        logger.error("Error while evaluating diagnosis: {}", ex.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    });
        } catch (Exception ex) {
            logger.error("Unexpected error: {}", ex.getMessage());
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            );
        }
    }



}
