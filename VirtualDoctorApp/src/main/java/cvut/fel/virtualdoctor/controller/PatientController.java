package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.PatientDTO;
import cvut.fel.virtualdoctor.model.Patient;
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
        Patient patient = new Patient(
                patientDTO.name(),
                patientDTO.age(),
                patientDTO.height(),
                patientDTO.weight(),
                patientDTO.gender(),
                patientDTO.location(),
                patientDTO.lifestyle()
        );
        patientService.createPatient(patient);
    }
}
