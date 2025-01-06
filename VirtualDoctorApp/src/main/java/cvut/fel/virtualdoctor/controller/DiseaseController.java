package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.DiseaseDetailsDTO;
import cvut.fel.virtualdoctor.dto.mapper.DiseaseDetailsMapper;
import cvut.fel.virtualdoctor.exception.NotFoundException;
import cvut.fel.virtualdoctor.model.DiseaseDetails;
import cvut.fel.virtualdoctor.service.DiseaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/disease")
@AllArgsConstructor
public class DiseaseController implements IDiseaseController {

    private DiseaseService diseaseService;

    @GetMapping("/long")
    public ResponseEntity<DiseaseDetailsDTO> getDiseaseDetails(@RequestParam String disease) {
        try {
            DiseaseDetails diseaseDetails = diseaseService.getDiseaseDetails(disease);
            return ResponseEntity.ok(DiseaseDetailsMapper.toDTO(diseaseDetails));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/short")
    public ResponseEntity<String> getShortDescription(@RequestParam String disease) {
        try {
            return ResponseEntity.ok(diseaseService.getShortDescription(disease));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(String.format("Disease %s not found!", disease));
        }
    }
}
