package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.service.DiagnosisService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("api/diagnosis")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @PostMapping("/mark")
    public ResponseEntity<String> markDiagnosis(UUID diagnosisId, String disease) {
        try {
            diagnosisService.markDiagnosis(diagnosisId, disease);
            return ResponseEntity.ok("Diagnosis " + diagnosisId + " marked as " + disease);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error marking diagnosis " + diagnosisId + ": " + e.getMessage());
        }
    }
}
