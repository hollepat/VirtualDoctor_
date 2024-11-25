package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.PatientDTO;
import cvut.fel.virtualdoctor.service.PatientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("patient")
public class PatientController implements IPatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/new-patient")
    public void createUser(@RequestBody PatientDTO patientDTO) {
        patientService.createUser(patientDTO);
    }
}
