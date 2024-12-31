package cvut.fel.virtualdoctor.classifier.client;

import java.util.concurrent.CompletableFuture;

public interface IClassifierClient {

    CompletableFuture<ClassifierOutput> getPrediction(ClassifierInput classifierInput);
}
