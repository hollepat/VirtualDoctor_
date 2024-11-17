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

    /**
     * Creates a diagnosis based on the user input and the differential list.
     *
     * @param userInput user input
     * @param swVersion classifying software version
     * @param differentialList list of diseases and their probabilities
     * @return the created diagnosis
     */
    public Diagnosis createDiagnosis(UserInput userInput, String swVersion, DifferentialList differentialList) {
        List<DoctorType> doctorToVisit = retrieveDoctorToVisit(differentialList);
        EmergencyType emergency = determineEmergency(userInput);

        return new Diagnosis(swVersion, LocalDateTime.now(), differentialList, doctorToVisit, emergency);
    }

    private EmergencyType determineEmergency(UserInput userInput) {
        return userInput.getSymptoms().stream()
                .map(Symptom::getEmergency)
                .max(EmergencyType.LEVEL_COMPARATOR)
                .orElse(EmergencyType.NORMAL);
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
