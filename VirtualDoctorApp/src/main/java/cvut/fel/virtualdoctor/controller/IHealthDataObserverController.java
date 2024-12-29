package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.HealthDataDTO;
import org.springframework.http.ResponseEntity;

public interface IHealthDataObserverController {

    ResponseEntity<String> saveHealthData(HealthDataDTO healthDataDTO);
}
