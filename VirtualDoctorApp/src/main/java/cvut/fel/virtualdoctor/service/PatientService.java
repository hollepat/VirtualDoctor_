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

    /**
     * Create a new patient profile with the given name.
     * @param patient patient to create
     * @return the created patient
     */
    public Patient createPatient(Patient patient) {
        if (patientRepository.findByName(patient.getName()).isPresent()) {
            logger.error("Patient with name {} already exists", patient.getName());
            return null;
        }
        logger.info("Creating new name...");
        return patientRepository.save(patient);
    }

    public Patient findByName(String name) {
        return patientRepository.findByName(name).orElseThrow(
                () -> new RuntimeException("Patient with name " + name + " not found")
        );
    }
}
