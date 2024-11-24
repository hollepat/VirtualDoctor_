package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.PatientDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface IPatientController {

    @PostMapping("/new-user")
    void createUser(@RequestBody PatientDTO patientDTO);
}