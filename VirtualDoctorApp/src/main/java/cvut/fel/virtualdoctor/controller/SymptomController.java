package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.SymptomDTO;
import cvut.fel.virtualdoctor.model.Symptom;
import cvut.fel.virtualdoctor.service.SymptomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/symptoms")
@AllArgsConstructor
public class SymptomController {

    private final SymptomService symptomService;

    @GetMapping("/all")
    public ResponseEntity<List<Symptom>> getAllSymptoms() {
        return ResponseEntity.ok(symptomService.getAllSymptoms());
    }

    // Service endpoint for adding a new symptom
    @PostMapping("/new-symptom")
    public void addSymptom(@RequestBody SymptomDTO symptomDTO) {
        // convert DTO to entity
        Symptom symptom = new Symptom(symptomDTO.name(), symptomDTO.description());
        // call service
        symptomService.addSymptom(symptom);
    }

    // Service endpoint for deleting a symptom
    @DeleteMapping("/delete-symptom")
    public void deleteSymptom(@RequestBody SymptomDTO symptomDTO) {
        // convert DTO to entity
        Symptom symptom = new Symptom(symptomDTO.name(), symptomDTO.description());
        // call service
        symptomService.deleteSymptom(symptom);
    }

}
