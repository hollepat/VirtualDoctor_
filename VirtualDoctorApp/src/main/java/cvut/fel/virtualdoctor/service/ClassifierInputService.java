package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.classifier.client.ClassifierInput;
import cvut.fel.virtualdoctor.classifier.mapper.ClassifierMapper;
import cvut.fel.virtualdoctor.model.ClassifierInputEntity;
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

//    public ClassifierInputEntity save(ClassifierInputEntity classifierInputEntity) {
//        try {
//            logger.info("Saving classifier input={}", classifierInputEntity);
//            return classifierInputRepository.save(classifierInputEntity);
//        } catch (Exception e) {
//            logger.error("Failed to save classifier input={}", classifierInputEntity, e);
//            throw new RuntimeException("Failed to save classifier input=" + classifierInputEntity, e);
//        }
//    }

    public ClassifierInputEntity createClassifierInput(PatientInput patientInput, HealthData healthData) {
        ClassifierInputEntity classifierInputEntity = ClassifierMapper.mapUserInputToClassifierInputEntity(patientInput, healthData);

        try {
            logger.info("Saving classifier input={}", classifierInputEntity);
            classifierInputRepository.save(classifierInputEntity);
        } catch (Exception e) {
            logger.error("Failed to save classifier input={}", classifierInputEntity, e);
            throw new RuntimeException("Failed to save classifier input=" + classifierInputEntity, e);
        }
        return classifierInputEntity;
    }
//
//    private static ClassifierInputEntity getClassifierInputEntity(PatientInput patientInput, ClassifierInput classifierInput) {
//        ClassifierInputEntity entity = new ClassifierInputEntity();
//        entity.setAge(classifierInput.age());
//        entity.setLifestyle(classifierInput.lifestyle());
//        entity.setGender(classifierInput.gender());
//        entity.setLocation(classifierInput.location());
//        entity.setSymptomsFromList(classifierInput.symptoms());
//        entity.setCholesterolLevel(classifierInput.cholesterolLevel());
//        entity.setHealthDataFromMap(classifierInput.healthData());
//        entity.setPatientInput(patientInput);
//        return entity;
//    }
}