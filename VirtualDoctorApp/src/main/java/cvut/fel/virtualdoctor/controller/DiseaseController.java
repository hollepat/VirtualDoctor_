package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.exception.NotFoundException;
import cvut.fel.virtualdoctor.service.DiseaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/disease")
@AllArgsConstructor
public class DiseaseController implements IDiseaseController {

    private DiseaseService diseaseService;

    @GetMapping("/long")
    public ResponseEntity<String> getLongDescription(@RequestParam String disease) {
        try {
            return ResponseEntity.ok(diseaseService.getShortDescription(disease));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(String.format("Disease %s not found!", disease));
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
