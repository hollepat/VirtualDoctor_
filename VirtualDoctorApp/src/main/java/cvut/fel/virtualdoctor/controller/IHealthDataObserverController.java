package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.VitalSignsDTO;
import org.springframework.http.ResponseEntity;

public interface IHealthDataObserverController {

    ResponseEntity<String> saveVitalSigns(VitalSignsDTO vitalSignsDTO);
}
