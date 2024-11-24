package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.dto.PatientDTO;
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

    public void createUser(PatientDTO patientDTO) {
        logger.info("Creating new name...");
        Patient patient = new Patient(
                patientDTO.name(),
                patientDTO.age(),
                patientDTO.height(),
                patientDTO.weight(),
                patientDTO.gender(),
                patientDTO.location(),
                patientDTO.lifestyle()
        );
        patientRepository.save(patient);
    }

    public Patient getUser(String name) {
        logger.info("Getting name...");
        return patientRepository.findByName(name).orElseThrow(
                () -> new RuntimeException("User not found")
        );
    }
}
