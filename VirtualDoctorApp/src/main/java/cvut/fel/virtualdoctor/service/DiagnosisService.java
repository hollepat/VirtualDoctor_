package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.model.*;
import cvut.fel.virtualdoctor.repository.DiagnosisRepository;
import cvut.fel.virtualdoctor.repository.DiseaseRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class DiagnosisService implements IDiagnosisService {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosisService.class);

    DiseaseRepository diseaseRepository;
    DiagnosisRepository diagnosisRepository;

    public Diagnosis createDiagnosis(String swVersion, DifferentialList differentialList) {
        List<DoctorType> doctorToVisit = retrieveDoctorToVisit(differentialList);
        return new Diagnosis(swVersion, LocalDateTime.now(), differentialList, doctorToVisit, EmergencyType.LOW);
    }

    private List<DoctorType> retrieveDoctorToVisit(DifferentialList differentialList) {
        return differentialList.getDdx().entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))
                .map(entry -> {
                    Disease disease = diseaseRepository.findDiseaseByName(entry.getKey())
                            .orElseThrow(() -> new RuntimeException("Disease not found"));
                    return disease.getDoctor();
                })
                .collect(Collectors.toCollection(LinkedHashSet::new))
                .stream()
                .toList();

    }

    public void saveDiagnosis(Diagnosis diagnosis) {
        logger.info("Saving diagnosis...");
        diagnosisRepository.save(diagnosis);
    }

}
