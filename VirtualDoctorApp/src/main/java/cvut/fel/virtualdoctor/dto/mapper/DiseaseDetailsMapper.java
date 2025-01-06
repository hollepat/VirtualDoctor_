package cvut.fel.virtualdoctor.dto.mapper;

import cvut.fel.virtualdoctor.dto.DiseaseDetailsDTO;
import cvut.fel.virtualdoctor.model.DiseaseDetails;
import cvut.fel.virtualdoctor.model.Medication;

import java.util.List;

public class DiseaseDetailsMapper {

    public static DiseaseDetailsDTO toDTO(DiseaseDetails details) {
        return new DiseaseDetailsDTO(
                details.getId(),
                details.getDescription(),
                details.getMedications().stream().map(Medication::getName).toList(),
                details.getTreatments(),
                details.getPrecautions()
        );
    }
}