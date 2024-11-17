package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.DiagnosisDTO;
import cvut.fel.virtualdoctor.dto.UserInputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.CompletableFuture;

public interface IEvaluatorController {

    @PostMapping("/evaluate")
    CompletableFuture<ResponseEntity<DiagnosisDTO>> evaluateDiagnosis(@RequestBody UserInputDTO userInputDTO);
}