package cvut.fel.virtualdoctor.classifier.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record ClassifierOutput(
        @JsonProperty("service_version") String version,
        Map<String, Double> predictions
) {
}
