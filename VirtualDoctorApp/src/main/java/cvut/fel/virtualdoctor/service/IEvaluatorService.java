package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.dto.DiagnosisDTO;
import cvut.fel.virtualdoctor.dto.PatientInputDTO;

import java.util.concurrent.CompletableFuture;

public interface IEvaluatorService {
    CompletableFuture<DiagnosisDTO> evaluateUserInput(PatientInputDTO patientInputDTO);
}
