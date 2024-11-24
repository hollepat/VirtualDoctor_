package cvut.fel.virtualdoctor.classifier.mapper;

import cvut.fel.virtualdoctor.classifier.client.ClassifierInput;
import cvut.fel.virtualdoctor.model.Symptom;
import cvut.fel.virtualdoctor.model.PatientInput;
import cvut.fel.virtualdoctor.model.VitalSigns;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ClassifierMapper {

    /**
     * Maps the name input to the classifier input. This allows to adapt the name input to the classifier input e.g.
     * by adding additional information or by changing the format of the data such as naming.
     *
     * @param patientInput the name input
     * @return the classifier input
     */
    public ClassifierInput mapUserInputToEvaluatorInput(PatientInput patientInput) {

        // map Vital Signs to JSON
        VitalSigns vitalSigns = patientInput.getVitalSigns();
        Map<String, Double> vitalSignsJson = vitalsToMap(vitalSigns);

        return new ClassifierInput(
                patientInput.getPatient().getAge(),
                patientInput.getPatient().getHeight(),
                patientInput.getPatient().getWeight(),
                patientInput.getPatient().getLifestyle(),
                patientInput.getPatient().getGender(),
                patientInput.getPatient().getLocation(),
                patientInput.getSymptoms().stream().map(Symptom::getName).toList(),
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
