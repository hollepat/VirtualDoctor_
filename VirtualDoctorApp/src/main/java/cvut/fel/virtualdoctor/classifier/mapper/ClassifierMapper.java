package cvut.fel.virtualdoctor.classifier.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import cvut.fel.virtualdoctor.classifier.client.ClassifierInput;
import cvut.fel.virtualdoctor.model.Symptom;
import cvut.fel.virtualdoctor.model.UserInput;
import cvut.fel.virtualdoctor.model.VitalSigns;
import cvut.fel.virtualdoctor.repository.DiseaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ClassifierMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ClassifierMapper(DiseaseRepository diseaseRepository) {
    }

    private static final Logger logger = LoggerFactory.getLogger(ClassifierMapper.class);

    /**
     * Maps the user input to the classifier input. This allows to adapt the user input to the classifier input e.g.
     * by adding additional information or by changing the format of the data such as naming.
     *
     * @param userInput the user input
     * @return the classifier input
     */
    public ClassifierInput mapUserInputToEvaluatorInput(UserInput userInput) {

        // map Vital Signs to JSON
        VitalSigns vitalSigns = userInput.getVitalSigns();
        Map<String, Double> vitalSignsJson = vitalsToMap(vitalSigns);

        return new ClassifierInput(
                userInput.getUser().getAge(),
                userInput.getUser().getHeight(),
                userInput.getUser().getWeight(),
                userInput.getUser().getLifestyle(),
                userInput.getUser().getGender(),
                userInput.getUser().getLocation(),
                userInput.getSymptoms().stream().map(Symptom::getName).toList(),
                vitalSignsJson
        );
    }

    private Map<String, Double> vitalsToMap(VitalSigns vitalSigns) {
        Map<String, Double> vitalSignsJson = new HashMap<>();
        vitalSignsJson.put("Blood Pressure", vitalSigns.getBloodPressure());
        vitalSignsJson.put("Cholesterol Level", vitalSigns.getCholesterolLevel());
        vitalSignsJson.put("Temperature", vitalSigns.getTemperature());
        vitalSignsJson.put("BMI", vitalSigns.getBmi());
        return vitalSignsJson;
    }
}
