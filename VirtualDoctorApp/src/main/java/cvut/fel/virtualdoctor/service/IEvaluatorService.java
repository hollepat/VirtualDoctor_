package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.model.Diagnosis;
import cvut.fel.virtualdoctor.model.PatientInput;

import java.util.concurrent.CompletableFuture;

public interface IEvaluatorService {
    CompletableFuture<Diagnosis> evaluateUserInput(PatientInput patientInput);
}
