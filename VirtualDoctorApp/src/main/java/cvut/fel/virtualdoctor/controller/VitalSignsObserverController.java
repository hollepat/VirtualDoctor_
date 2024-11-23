package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.VitalSignsDTO;
import cvut.fel.virtualdoctor.service.VitalSignsObserver;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vitalSignsObserver")
@AllArgsConstructor
public class VitalSignsObserverController implements IVitalSignsObserverController {

    private final VitalSignsObserver vitalSignsObserver;

    @PostMapping("/vitalSigns")
    public ResponseEntity<String> saveVitalSigns(@RequestBody VitalSignsDTO vitalSignsDTO) {
        vitalSignsObserver.update(vitalSignsDTO);
        return ResponseEntity.ok("Data received");
    }
}
