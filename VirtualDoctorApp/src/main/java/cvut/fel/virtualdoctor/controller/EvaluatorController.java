package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.DiagnosisDTO;
import cvut.fel.virtualdoctor.dto.UserInputDTO;
import cvut.fel.virtualdoctor.service.EvaluatorService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/evaluation")
@AllArgsConstructor
public class EvaluatorController implements IEvaluatorController {

    private static final Logger logger = LoggerFactory.getLogger(EvaluatorController.class);
    private final EvaluatorService evaluatorServiceImpl;

    @PostMapping("/evaluate")
    public CompletableFuture<ResponseEntity<DiagnosisDTO>> evaluateDiagnosis(@RequestBody UserInputDTO userInputDTO) {
        logger.info("Received request to evaluate diagnosis");

        // Evaluate diagnosis
        return evaluatorServiceImpl.evaluateUserInput(userInputDTO).thenApply(diagnosisDTO -> {
            logger.info("Diagnosis sent: {}", diagnosisDTO.toString());
            return ResponseEntity.ok(diagnosisDTO);
        });
    }



}
