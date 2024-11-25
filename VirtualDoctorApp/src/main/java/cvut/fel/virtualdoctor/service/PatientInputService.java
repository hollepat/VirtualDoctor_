package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.model.PatientInput;
import cvut.fel.virtualdoctor.repository.PatientInputRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PatientInputService implements IPatientInputService {

    PatientInputRepository patientInputRepository;

    public PatientInput save(PatientInput patientInput) {
        return patientInputRepository.save(patientInput);
    }
}
