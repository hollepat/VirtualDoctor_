package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.DiagnosisDTO;
import cvut.fel.virtualdoctor.dto.PatientInputDTO;
import cvut.fel.virtualdoctor.dto.mapper.DiagnosisMapper;
import cvut.fel.virtualdoctor.model.Patient;
import cvut.fel.virtualdoctor.model.PatientInput;
import cvut.fel.virtualdoctor.model.Symptom;
import cvut.fel.virtualdoctor.repository.PatientRepository;
import cvut.fel.virtualdoctor.repository.SymptomRepository;
import cvut.fel.virtualdoctor.service.EvaluatorService;
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
@RequestMapping("/api/v1/evaluation")
@AllArgsConstructor
public class EvaluatorController implements IEvaluatorController {

    private static final Logger logger = LoggerFactory.getLogger(EvaluatorController.class);
    private final EvaluatorService evaluatorServiceImpl;
    private final PatientRepository patientRepository;
    private final SymptomRepository symptomRepository;
    private final DiagnosisMapper diagnosisMapper;

    @PostMapping("/evaluate")
    public CompletableFuture<ResponseEntity<DiagnosisDTO>> evaluateDiagnosis(@RequestBody PatientInputDTO patientInputDTO) {
        logger.info("Received request to evaluate diagnosis");

        // TODO move to service
        Patient patient = patientRepository.findByName(patientInputDTO.name())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // TODO move to service
        List<Symptom> symptoms = patientInputDTO.symptoms().stream().map(
                symptomName -> symptomRepository.findByName(symptomName)
                        .orElseThrow(() -> new RuntimeException("Symptom not found"))
        ).toList();

        PatientInput patientInput = new PatientInput(patient, symptoms, patientInputDTO.cholesterolLevel());


        // Evaluate diagnosis
        return evaluatorServiceImpl.evaluateUserInput(patientInput).thenApply(diagnosis -> {
            DiagnosisDTO diagnosisDTO = diagnosisMapper.toDTO(diagnosis);
            logger.info("Diagnosis sent: {}", diagnosisDTO.toString());
            return ResponseEntity.ok(diagnosisDTO);
        });
    }



}
