package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.PatientDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface IPatientController {

    @PostMapping("/new-user")
    ResponseEntity<String> createUser(@RequestBody PatientDTO patientDTO);
}