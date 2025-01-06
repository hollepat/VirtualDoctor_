package cvut.fel.virtualdoctor.dto;

import java.util.List;
import java.util.UUID;

public record DiseaseDetailsDTO(
        UUID id,
        String description,
        List<String> medications,
        List<String> treatments,
        List<String> precautions
) {}