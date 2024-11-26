package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.model.Patient;
import cvut.fel.virtualdoctor.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PatientService implements IPatientService {

    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

    PatientRepository patientRepository;

    public Patient createPatient(Patient patient) {
        logger.info("Creating new name...");
        return patientRepository.save(patient);
    }

    public Patient findByName(String name) {
        logger.info("Getting name...");
        return patientRepository.findByName(name).orElseThrow(
                () -> new RuntimeException("User not found")
        );
    }
}
