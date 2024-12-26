package cvut.fel.virtualdoctor.classifier.client;

import cvut.fel.virtualdoctor.model.PatientInput;
import cvut.fel.virtualdoctor.model.HealthData;

import java.util.concurrent.CompletableFuture;

public interface IClassifierClient {

    CompletableFuture<ClassifierOutput> getPrediction(PatientInput patientInput, HealthData healthData);
}
