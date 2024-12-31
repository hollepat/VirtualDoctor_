package cvut.fel.virtualdoctor.classifier.client;

import cvut.fel.virtualdoctor.model.ClassifierInput;

import java.util.concurrent.CompletableFuture;

public interface IClassifierClient {

    CompletableFuture<ClassifierOutputDTO> getPrediction(ClassifierInput classifierInput);
}
