package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.classifier.client.ClassifierOutput;
import cvut.fel.virtualdoctor.model.*;
import cvut.fel.virtualdoctor.repository.DiagnosisRepository;
import cvut.fel.virtualdoctor.repository.DifferentialListRepository;
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
    DifferentialListRepository differentialListRepository;

    /**
     * Creates a diagnosis based on the name input and the differential list.
     *
     * @param patientInput name input
     * @param classifierOutput response from the classifier
     * @return the created diagnosis
     */
    public Diagnosis createDiagnosis(PatientInput patientInput, ClassifierOutput classifierOutput) {
        DifferentialList differentialList = differentialListRepository.save(new DifferentialList(classifierOutput.predictions()));

        String swVersion = classifierOutput.version();
        List<DoctorType> doctorToVisit = retrieveDoctorToVisit(differentialList);
        EmergencyType emergency = determineEmergency(patientInput);

        Diagnosis diagnosis = new Diagnosis(
                swVersion,
                LocalDateTime.now(),
                differentialList,
                doctorToVisit,
                emergency,
                patientInput
        );
        save(diagnosis);
        return diagnosis;
    }

    private EmergencyType determineEmergency(PatientInput patientInput) {
        return patientInput.getSymptoms().stream()
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

    public void save(Diagnosis diagnosis) {
        try {
            diagnosisRepository.save(diagnosis);
            logger.info("Saved diagnosis for patient {}", diagnosis.getPatientInput().getPatient().getName());
        } catch (Exception e) {
            logger.error("Error saving diagnosis: {}", e.getMessage());
            throw e;
        }
    }

}
