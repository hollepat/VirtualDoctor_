package cvut.fel.virtualdoctor.classifier.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ClassifierOutput fromJson(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, ClassifierOutput.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON string to object: " + e.getMessage(), e);
        }
    }
}