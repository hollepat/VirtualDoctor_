package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.dto.DiagnosisDTO;
import cvut.fel.virtualdoctor.dto.UserInputDTO;

import java.util.concurrent.CompletableFuture;

public interface IEvaluatorService {
    CompletableFuture<DiagnosisDTO> evaluateUserInput(UserInputDTO userInputDTO);
}
