package cvut.fel.virtualdoctor.classifier.mapper;

import cvut.fel.virtualdoctor.classifier.client.ClassifierInput;
import cvut.fel.virtualdoctor.model.Symptom;
import cvut.fel.virtualdoctor.model.PatientInput;
import cvut.fel.virtualdoctor.model.HealthData;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ClassifierMapper {

    /**
     * Maps the name input to the classifier input. This allows to adapt the name input to the classifier input e.g.
     * by adding additional information or by changing the format of the data such as naming.
     *
     * @param patientInput input from patient
     * @param healthData the vital signs of the patient
     * @return the classifier input
     */
    public ClassifierInput mapUserInputToEvaluatorInput(PatientInput patientInput, HealthData healthData) {

        // map Vital Signs to JSON
        Map<String, Double> vitalSignsJson = vitalsToMap(healthData);

        return new ClassifierInput(
                patientInput.getPatient().getAge(),
                patientInput.getPatient().getLifestyle(),
                patientInput.getPatient().getGender(),
                patientInput.getPatient().getLocation(),
                patientInput.getSymptoms().stream().map(Symptom::getName).toList(),
                patientInput.getCholesterolLevel(),
                vitalSignsJson
        );
    }

    private Map<String, Double> vitalsToMap(HealthData healthData) {
        Map<String, Double> vitalSignsJson = new HashMap<>();
        vitalSignsJson.put("Blood Pressure", healthData.getBloodPressure());
        vitalSignsJson.put("Temperature", healthData.getSkinTemperature());
        vitalSignsJson.put("BMI", healthData.getBmi());
        return vitalSignsJson;
    }
}
