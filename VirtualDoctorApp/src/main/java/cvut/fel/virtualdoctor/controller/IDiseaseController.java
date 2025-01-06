package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.DiseaseDetailsDTO;
import org.springframework.http.ResponseEntity;

public interface IDiseaseController {
    ResponseEntity<DiseaseDetailsDTO> getDiseaseDetails(String disease);
    ResponseEntity<String> getShortDescription(String disease);
}
