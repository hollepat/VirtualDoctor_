package cvut.fel.virtualdoctor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SymptomConfig {
    @Value("#{'${app.symptoms.available}'.split(',')}")
    private List<String> availableSymptoms;

    public List<String> getAvailableSymptoms() {
        return availableSymptoms;
    }
}