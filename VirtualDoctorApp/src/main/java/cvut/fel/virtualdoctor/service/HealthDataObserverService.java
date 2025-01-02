package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.exception.MissingHealthData;
import cvut.fel.virtualdoctor.model.Patient;
import cvut.fel.virtualdoctor.model.HealthData;
import cvut.fel.virtualdoctor.repository.HealthDataRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class HealthDataObserverService implements IHealthDataObserverService {

    private final Logger logger = LoggerFactory.getLogger(HealthDataObserverService.class);

    HealthDataRepository healthDataRepository;
    PatientService patientService;

    @Override
    public void update(HealthData healthData) {
        healthDataRepository.save(healthData);
        logger.info("Vital signs updated: " + healthData);
    }

    /**
     * Provide health data for a name by computing the average of all the health data taken for that patient
     * today.
     * @param patient name for which health data are needed
     * @return average of health data taken today
     */
    @Override
    public HealthData provideHealthData(Patient patient) throws MissingHealthData {
        LocalDate today = LocalDate.now();
        logger.info("Providing health data for name: " + patient.toString());
        List<HealthData> recentHealthData = healthDataRepository.findByPatientName(patient.getName()).stream()
                .filter(vitalSign -> vitalSign.getLocalDateTime().toLocalDate().equals(today))
                .toList();


        if (recentHealthData.isEmpty()) {
            throw new MissingHealthData("No health data taken today for name: " + patient.getName());
        }

        return transformHealthData(recentHealthData);
    }

    /**
     * Transform a list of health data into a single health data by applying proper transformation functions. Transformations
     * include averaging the skin temperature, blood pressure, and heart rate, and taking the BMI from the last health data entry.
     *
     * @param healthData list of health data to transform
     * @return transformed health data
     */
    private HealthData transformHealthData(List<HealthData> healthData) {
        return new HealthData(
                healthData.get(0).getPatient(),
                LocalDateTime.now(), // TODO add global timer for the project --> more control and better for testing
                healthData.stream().mapToDouble(HealthData::getSkinTemperature).average().orElse(0),
                healthData.stream().mapToDouble(HealthData::getBloodPressure).average().orElse(0),
                healthData.get(healthData.size()-1).getBmi(),
                (int) healthData.stream().mapToDouble(HealthData::getHeartRate).average().orElse(0)
        );
    }
}