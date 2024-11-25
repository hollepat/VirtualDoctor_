package cvut.fel.virtualdoctor.dto;

import java.util.List;

public record PatientInputDTO(
    String name,
    List<String> symptoms,
    Double cholesterolLevel
) {
}