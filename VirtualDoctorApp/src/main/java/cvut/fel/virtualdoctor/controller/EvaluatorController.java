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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
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

        Patient patient = patientService.findByName(patientInputDTO.name());
        logger.info("Patient found: {}", patient.toString());

        List<Symptom> symptoms = patientInputDTO.symptoms().stream().map(symptomService::findByName).toList();
        PatientInput patientInput = new PatientInput(patient, symptoms, patientInputDTO.cholesterolLevel());

        // Evaluate diagnosis asynchronously
        return evaluatorService.evaluatePatientInput(patientInput).thenApply(diagnosis -> { // TODO: how to model in sequence diagram?
            DiagnosisDTO diagnosisDTO = diagnosisMapper.toDTO(diagnosis);
            logger.info("Diagnosis sent: {}", diagnosisDTO.toString());
            return ResponseEntity.ok(diagnosisDTO);
        });
    }



}
