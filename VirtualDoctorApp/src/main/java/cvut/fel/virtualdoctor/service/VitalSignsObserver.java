package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.dto.VitalSignsDTO;
import cvut.fel.virtualdoctor.exception.MissingHealthData;
import cvut.fel.virtualdoctor.model.Patient;
import cvut.fel.virtualdoctor.model.VitalSigns;
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
public class VitalSignsObserver implements IVitalSignsObserver {

    private final Logger logger = LoggerFactory.getLogger(VitalSignsObserver.class);

    VitalSignsRepository vitalSignsRepository;
    PatientService patientService;

    @Override
    public void update(VitalSignsDTO vitalSignsDTO) {
        logger.info("Vital signs updated: " + vitalSignsDTO.toString());
        Patient patient = patientService.getUser(vitalSignsDTO.name());

        VitalSigns vitalSigns = new VitalSigns(
                patient,
                LocalDateTime.now(),
                vitalSignsDTO.skinTemperature(),
                vitalSignsDTO.bloodPressure(),
                patient.getBmi(),
                150, // TODO resolve how to input this value --> shouldn't be from smart watch
                vitalSignsDTO.heartRate()
        );

        vitalSignsRepository.save(vitalSigns);
    }

    /**
     * Provide vital signs for a name by computing the average of all the vital signs taken for that name
     * from today.
     * @param patient name for which vital signs are needed
     * @return average of vital signs taken today
     */
    @Override
    public VitalSigns provideVitalSigns(Patient patient) throws MissingHealthData {
        LocalDate today = LocalDate.now();
        logger.info("Providing vital signs for name: " + patient.toString());
        List<VitalSigns> recentVitalSigns = vitalSignsRepository.findByName(patient.getName()).stream()
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
    private VitalSigns transformVitalSigns(List<VitalSigns> vitalSigns) {
        return new VitalSigns(
                vitalSigns.get(0).getPatient(),
                LocalDateTime.now(),
                vitalSigns.stream().mapToDouble(VitalSigns::getSkinTemperature).average().orElse(0),
                vitalSigns.stream().mapToDouble(VitalSigns::getBloodPressure).average().orElse(0),
                vitalSigns.get(vitalSigns.size()-1).getBmi(), 
                vitalSigns.stream().mapToDouble(VitalSigns::getCholesterolLevel).average().orElse(0),
                (int) vitalSigns.stream().mapToDouble(VitalSigns::getHeartRate).average().orElse(0)
        );
    }
}