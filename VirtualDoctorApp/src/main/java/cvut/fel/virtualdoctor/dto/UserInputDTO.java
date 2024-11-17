package cvut.fel.virtualdoctor.dto;

import java.util.List;

public record UserInputDTO (
    String user,
    List<String> symptoms
) {
}