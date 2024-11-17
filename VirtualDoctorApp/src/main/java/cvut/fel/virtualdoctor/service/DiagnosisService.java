package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.model.Diagnosis;
import cvut.fel.virtualdoctor.model.DifferentialList;
import cvut.fel.virtualdoctor.model.DoctorType;
import cvut.fel.virtualdoctor.model.EmergencyType;
import cvut.fel.virtualdoctor.repository.DiagnosisRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class DiagnosisService implements IDiagnosisService {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosisService.class);
    DiagnosisRepository diagnosisRepository;

    public Diagnosis createDiagnosis(String swVersion, DifferentialList differentialList) {
        return new Diagnosis(swVersion, LocalDateTime.now(), differentialList, DoctorType.ORL, EmergencyType.LOW);
    }

    public void saveDiagnosis(Diagnosis diagnosis) {
        logger.info("Saving diagnosis...");
        diagnosisRepository.save(diagnosis);
    }

}
