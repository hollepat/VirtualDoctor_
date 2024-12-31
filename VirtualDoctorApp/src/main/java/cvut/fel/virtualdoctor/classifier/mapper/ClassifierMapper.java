package cvut.fel.virtualdoctor.classifier.mapper;

import cvut.fel.virtualdoctor.classifier.client.ClassifierInput;
import cvut.fel.virtualdoctor.model.ClassifierInputEntity;
import cvut.fel.virtualdoctor.model.Symptom;
import cvut.fel.virtualdoctor.model.PatientInput;
import cvut.fel.virtualdoctor.model.HealthData;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

public class ClassifierMapper {

    /**
     * Maps the name input to the classifier input. This allows to adapt the name input to the classifier input e.g.
     * by adding additional information or by changing the format of the data such as naming.
     *
     * @param patientInput input from patient
     * @param healthData the vital signs of the patient
     * @return the classifier input
     */
    public static ClassifierInput mapClassifierInputToClassifierInputDTO(ClassifierInputEntity classifierInputEntity) {

        return new ClassifierInput(
                classifierInputEntity.getAge(),
                classifierInputEntity.getLifestyle(),
                classifierInputEntity.getGender(),
                classifierInputEntity.getLocation(),
                classifierInputEntity.getSymptomsAsList(),
                classifierInputEntity.getCholesterolLevel(),
                classifierInputEntity.getHealthDataAsMap()
        );
    }

    public static ClassifierInputEntity mapUserInputToClassifierInputEntity(PatientInput patientInput, HealthData healthData) {

        // map Vital Signs to JSON
        Map<String, Double> vitalSignsJson = vitalsToMap(healthData);

        ClassifierInputEntity entity = new ClassifierInputEntity();
        entity.setAge(patientInput.getPatient().getAge());
        entity.setLifestyle(patientInput.getPatient().getLifestyle());
        entity.setGender(patientInput.getPatient().getGender());
        entity.setLocation(patientInput.getPatient().getLocation());
        entity.setSymptomsFromList(patientInput.getSymptoms().stream().map(Symptom::getName).toList());
        entity.setCholesterolLevel(patientInput.getCholesterolLevel());
        entity.setHealthDataFromMap(vitalSignsJson);
        entity.setPatientInput(patientInput);
        return entity;
    }

    private static Map<String, Double> vitalsToMap(HealthData healthData) {
        Map<String, Double> vitalSignsJson = new HashMap<>();
        vitalSignsJson.put("Blood Pressure", healthData.getBloodPressure());
        vitalSignsJson.put("Temperature", healthData.getSkinTemperature());
        vitalSignsJson.put("BMI", healthData.getBmi());
        return vitalSignsJson;
    }
}
