package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.classifier.mapper.ClassifierMapper;
import cvut.fel.virtualdoctor.model.ClassifierInput;
import cvut.fel.virtualdoctor.model.HealthData;
import cvut.fel.virtualdoctor.model.PatientInput;
import cvut.fel.virtualdoctor.repository.ClassifierInputRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ClassifierInputService {

    private final ClassifierInputRepository classifierInputRepository;

    private static final Logger logger = LoggerFactory.getLogger(EvaluatorService.class);

    /**
     * Create a classifier input entity from a patient input and health data.
     * @param patientInput patient input
     * @param healthData health data
     * @return classifier input entity
     */
    public ClassifierInput createClassifierInput(PatientInput patientInput, HealthData healthData) {
        ClassifierInput classifierInput = ClassifierMapper.mapUserInputToClassifierInputEntity(patientInput, healthData);

        try {
            logger.info("Saving classifier input={}", classifierInput);
            classifierInputRepository.save(classifierInput);
        } catch (Exception e) {
            logger.error("Failed to save classifier input={}", classifierInput, e);
            throw new RuntimeException("Failed to save classifier input=" + classifierInput, e);
        }
        return classifierInput;
    }

}