package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.VitalSignsDTO;
import cvut.fel.virtualdoctor.model.Patient;
import cvut.fel.virtualdoctor.model.VitalSigns;
import cvut.fel.virtualdoctor.service.PatientService;
import cvut.fel.virtualdoctor.service.VitalSignsObserverServiceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("vitalSignsObserver")
@AllArgsConstructor
public class VitalSignsObserverController implements IVitalSignsObserverController {

    private final VitalSignsObserverServiceService vitalSignsObserverService;
    private final PatientService patientService;

    @PostMapping("/vitalSigns")
    public ResponseEntity<String> saveVitalSigns(@RequestBody VitalSignsDTO vitalSignsDTO) {
        Patient patient = patientService.findByName(vitalSignsDTO.name());

        VitalSigns vitalSigns = new VitalSigns(
                patient,
                LocalDateTime.now(),
                vitalSignsDTO.skinTemperature(),
                vitalSignsDTO.bloodPressure(),
                patient.getBmi(),
                vitalSignsDTO.heartRate()
        );

        vitalSignsObserverService.update(vitalSigns);
        return ResponseEntity.ok("Data received");
    }
}
