package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.PatientDTO;
import cvut.fel.virtualdoctor.model.Patient;
import cvut.fel.virtualdoctor.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/patient")
public class PatientController implements IPatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/new-patient")
    public ResponseEntity<String> createUser(@RequestBody PatientDTO patientDTO) {
        Patient patient = new Patient(
                patientDTO.name(),
                patientDTO.age(),
                patientDTO.height(),
                patientDTO.weight(),
                patientDTO.gender(),
                patientDTO.location(),
                patientDTO.lifestyle()
        );

        Patient savedPatient = patientService.createPatient(patient);
        if (savedPatient == null) {
            return ResponseEntity.badRequest().body(String.format("Patient %s already exists!", patient.getName()));
        }
        return ResponseEntity.ok(String.format("Patient %s created successfully!", patient.getName()));
    }
}
