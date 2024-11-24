package cvut.fel.virtualdoctor.dto;

import cvut.fel.virtualdoctor.model.Gender;
import cvut.fel.virtualdoctor.model.Lifestyle;
import cvut.fel.virtualdoctor.model.Location;

public record PatientDTO(
    String name,
    int age,
    int height,
    int weight,
    Gender gender,
    Location location,
    Lifestyle lifestyle
) {
}
