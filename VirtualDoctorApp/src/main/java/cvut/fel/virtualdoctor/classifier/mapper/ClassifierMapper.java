package cvut.fel.virtualdoctor.classifier.mapper;

import cvut.fel.virtualdoctor.classifier.client.ClassifierInputDTO;
import cvut.fel.virtualdoctor.model.ClassifierInput;
import cvut.fel.virtualdoctor.model.Symptom;
import cvut.fel.virtualdoctor.model.PatientInput;
import cvut.fel.virtualdoctor.model.HealthData;

import java.util.HashMap;
import java.util.Map;

public class ClassifierMapper {

    /**
     * Maps the classifier input to the classifier input DTO.
     *
     * @param classifierInput input from patient
     * @return the DTO for classifier input
     */
    public static ClassifierInputDTO mapClassifierInputToClassifierInputDTO(ClassifierInput classifierInput) {

        return new ClassifierInputDTO(
                classifierInput.getAge(),
                classifierInput.getLifestyle(),
                classifierInput.getGender(),
                classifierInput.getLocation(),
                classifierInput.getSymptomsAsList(),
                classifierInput.getCholesterolLevel(),
                classifierInput.getHealthDataAsMap()
        );
    }

    public static ClassifierInput mapUserInputToClassifierInputEntity(PatientInput patientInput, HealthData healthData) {

        // map Vital Signs to JSON
        Map<String, Double> vitalSignsJson = vitalsToMap(healthData);

        ClassifierInput entity = new ClassifierInput();
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
