package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.exception.MissingHealthData;
import cvut.fel.virtualdoctor.model.Patient;
import cvut.fel.virtualdoctor.model.HealthData;
import cvut.fel.virtualdoctor.repository.VitalSignsRepository;
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

    VitalSignsRepository vitalSignsRepository;
    PatientService patientService;

    @Override
    public void update(HealthData healthData) {
        vitalSignsRepository.save(healthData);
        logger.info("Vital signs updated: " + healthData);
    }

    /**
     * Provide vital signs for a name by computing the average of all the vital signs taken for that name
     * from today.
     * @param patient name for which vital signs are needed
     * @return average of vital signs taken today
     */
    @Override
    public HealthData provideVitalSigns(Patient patient) throws MissingHealthData {
        LocalDate today = LocalDate.now();
        logger.info("Providing vital signs for name: " + patient.toString());
        List<HealthData> recentVitalSigns = vitalSignsRepository.findByPatientName(patient.getName()).stream()
                .filter(vitalSign -> vitalSign.getLocalDateTime().toLocalDate().equals(today))
                .toList();


        if (recentVitalSigns.isEmpty()) {
            throw new MissingHealthData("No vital signs taken today for name: " + patient.getName());
        }

        return transformVitalSigns(recentVitalSigns);
    }

    /**
     * Transform a list of vital signs into a single vital sign by applying proper transformation functions.
     * @param vitalSigns list of vital signs to transform
     * @return transformed vital sign
     */
    private HealthData transformVitalSigns(List<HealthData> vitalSigns) {
        return new HealthData(
                vitalSigns.get(0).getPatient(),
                LocalDateTime.now(),
                vitalSigns.stream().mapToDouble(HealthData::getSkinTemperature).average().orElse(0),
                vitalSigns.stream().mapToDouble(HealthData::getBloodPressure).average().orElse(0),
                vitalSigns.get(vitalSigns.size()-1).getBmi(), 
                (int) vitalSigns.stream().mapToDouble(HealthData::getHeartRate).average().orElse(0)
        );
    }
}