package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.classifier.client.ClassifierOutputDTO;
import cvut.fel.virtualdoctor.model.*;
import cvut.fel.virtualdoctor.repository.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class DiagnosisService implements IDiagnosisService {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosisService.class);

    DiseaseRepository diseaseRepository;
    DiagnosisRepository diagnosisRepository;
    DifferentialListRepository differentialListRepository;
    HealthDataRepository healthDataRepository;
    PatientDataRepository patientDataRepository;

    private final List<String> availableSymptoms = List.of(
            "Fever",
            "Cough",
            "Fatigue",
            "Difficulty breathing",
            "Headache",
            "Sore throat",
            "Runny nose"
    );

    /**
     * Creates a diagnosis based on the name input and the differential list.
     *
     * @param patientInput name input
     * @param classifierOutputDTO response from the classifier
     * @return the created diagnosis
     */
    public Diagnosis createDiagnosis(ClassifierInput classifierInput, PatientInput patientInput, ClassifierOutputDTO classifierOutputDTO) {
        DifferentialList differentialList = differentialListRepository.save(new DifferentialList(classifierOutputDTO.predictions()));

        String swVersion = classifierOutputDTO.version();
        List<DoctorType> doctorToVisit = retrieveDoctorToVisit(differentialList);
        EmergencyType emergency = determineEmergency(patientInput);

        Diagnosis diagnosis = new Diagnosis(
                swVersion,
                LocalDateTime.now(),
                differentialList,
                doctorToVisit,
                emergency,
                classifierInput
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
            logger.info("Saved diagnosis for patient {}", diagnosis.getClassifierInput().getPatientInput().getPatient().getName());
        } catch (Exception e) {
            logger.error("Error saving diagnosis: {}", e.getMessage());
            throw e;
        }
    }

    public void markDiagnosis(UUID diagnosisId, String disease) {
        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found"));

        ClassifierInput classifierInput = diagnosis.getClassifierInput();

        // Save the diagnosis to patient_data table
        PatientData patientData = new PatientData();

        patientData.setDisease(disease);

        patientData.setAge(classifierInput.getAge());
        patientData.setGender(classifierInput.getGender().getDisplayName());

        patientData.setCholesterolLevel(HealthUtils.convertCholesterolLevel(classifierInput.getCholesterolLevel(), classifierInput.getAge()));
        patientData.setOutcomeVariable("Positive"); // When adding a diagnosis, it is always positive

        Map<String, Double> healthDataAsMap = classifierInput.getHealthDataAsMap();

        // These keys have to align with ClassifierMapper.vitalsToMap
        patientData.setBloodPressure(HealthUtils.convertBloodPressure(healthDataAsMap.get("Blood Pressure")));
        patientData.setTemperature(healthDataAsMap.get("Temperature"));
        patientData.setBmi(healthDataAsMap.get("BMI"));

        // Base on symptoms set them "Yes" if in patient input and "No" if not
        List<String> insertedSymptoms = classifierInput.getSymptomsAsList();
        for (String symptom : availableSymptoms) {
            patientData.setSymptom(symptom, insertedSymptoms.contains(symptom));
        }

        patientDataRepository.save(patientData);
    }

}
