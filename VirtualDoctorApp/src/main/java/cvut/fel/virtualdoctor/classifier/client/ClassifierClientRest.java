package cvut.fel.virtualdoctor.classifier.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cvut.fel.virtualdoctor.classifier.mapper.ClassifierMapper;
import cvut.fel.virtualdoctor.model.PatientInput;
import cvut.fel.virtualdoctor.model.HealthData;
import org.springframework.javapoet.ClassName;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class ClassifierClientRest implements IClassifierClient {
    private static final Logger logger = Logger.getLogger(ClassName.class.getName());

    private final String url;
    private final ObjectMapper objectMapper;
    private final ClassifierMapper classifierMapper;

    public ClassifierClientRest(String url, ClassifierMapper classifierMapper) {
        this.url = url;
        this.objectMapper = new ObjectMapper();
        this.classifierMapper = classifierMapper;
    }

    /**
     * Send POST request to the model endpoint to trigger the classification. The response is a JSON with the classification
     * result.
     * @param patientInput The patient input data to evaluate
     * @param healthData The vital signs of the patient
     * @return The classification result as a JSON string. Wrapped in a CompletableFuture to allow for async processing.
     */
    public CompletableFuture<ClassifierOutput> getPrediction(PatientInput patientInput, HealthData healthData) {
        try {
            ClassifierInput classifierInput = classifierMapper.mapUserInputToEvaluatorInput(patientInput, healthData);
            String jsonBody = objectMapper.writeValueAsString(classifierInput);

            // Create the HTTP request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            // Send the request and return the response body
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> JsonUtil.fromJson(response.body()));

        } catch (JsonProcessingException e) {
            logger.warning("Failed to serialize model input to JSON: " + e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }
}
