package cvut.fel.virtualdoctor.dto;

import cvut.fel.virtualdoctor.model.Symptom;

public record SymptomDTO(
        String name,
        String description
) {
    public SymptomDTO(Symptom symptom) {
        this(symptom.getName(), symptom.getDescription());
    }
}
