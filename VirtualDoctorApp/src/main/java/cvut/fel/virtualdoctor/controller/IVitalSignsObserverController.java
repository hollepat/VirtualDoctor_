package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.VitalSignsDTO;
import org.springframework.http.ResponseEntity;

public interface IVitalSignsObserverController {

    ResponseEntity<String> saveVitalSigns(VitalSignsDTO vitalSignsDTO);
}
