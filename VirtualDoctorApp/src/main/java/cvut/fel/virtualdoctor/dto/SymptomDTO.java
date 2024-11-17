package cvut.fel.virtualdoctor.dto;

import cvut.fel.virtualdoctor.model.EmergencyType;
import cvut.fel.virtualdoctor.model.Symptom;

public record SymptomDTO(
        String name,
        EmergencyType emergency,
        String description
) {
    public SymptomDTO(Symptom symptom) {
        this(symptom.getName(), symptom.getEmergency(), symptom.getDescription());
    }
}
