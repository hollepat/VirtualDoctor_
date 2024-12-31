package cvut.fel.virtualdoctor.classifier.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record ClassifierOutputDTO(
        @JsonProperty("service_version") String version,
        Map<String, Double> predictions
) {
}
