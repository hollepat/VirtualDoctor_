package cvut.fel.virtualdoctor.config;

import cvut.fel.virtualdoctor.model.*;
import cvut.fel.virtualdoctor.repository.DiseaseRepository;
import cvut.fel.virtualdoctor.repository.SymptomRepository;
import cvut.fel.virtualdoctor.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Configuration
public class DataInit {

    private static final Logger logger = LoggerFactory.getLogger(DataInit.class);

    @Bean
    CommandLineRunner runner(DiseaseRepository diseaseRepository, SymptomRepository symptomRepository, PatientRepository patientRepository) {
        return args -> {
            // User
            createPatient("john-doe", 18, 170, 70, patientRepository);
            createPatient("jane-doe", 25, 160, 60, patientRepository);

            // Symptoms
            createSymptom("Headache", EmergencyType.NORMAL,"Pain in the head or upper neck.", symptomRepository);
            createSymptom("Fever", EmergencyType.STAY_AT_HOME, "A temporary increase in your body skinTemperature, often due to an illness.", symptomRepository);
            createSymptom("Cough", EmergencyType.NORMAL,"A sudden expulsion of air from the lungs that clears the air passages.", symptomRepository);
            createSymptom("Fatigue", EmergencyType.NORMAL,"A feeling of tiredness or exhaustion or a need to rest because of lack of energy or strength.", symptomRepository);
            createSymptom("Difficulty breathing", EmergencyType.LIFE_THREATENING, "A feeling of not being able to get enough air.", symptomRepository);
            createSymptom("Severe chest pain", EmergencyType.LIFE_THREATENING, "A feeling of pressure, squeezing, or fullness in the chest.", symptomRepository);


            // Diseases
            createDisease("Influenza", "A viral infection that attacks your respiratory system.", "Influenza is a viral infection that attacks your respiratory system — your nose, throat and lungs.", List.of(
                    symptomRepository.findByName("Headache").orElseThrow(),
                    symptomRepository.findByName("Fever").orElseThrow(),
                    symptomRepository.findByName("Cough").orElseThrow()
            ), DoctorType.GENERAL_PRACTITIONER, diseaseRepository);
            createDisease("Acne", "A skin condition that occurs when your hair follicles become plugged with oil and dead skin cells.", "Acne is a skin condition that occurs when your hair follicles become plugged with oil and dead skin cells.",
                    List.of(), DoctorType.DERMATOLOGY, diseaseRepository);
            createDisease("Allergic Rhinitis", "An allergic reaction to airborne allergens that causes cold-like symptoms.", "Allergic rhinitis is a collection of symptoms, predominantly in the nose and eyes, caused by airborne particles of dust, dander, or plant pollens in people who are allergic to these substances.", List.of(
                    symptomRepository.findByName("Cough").orElseThrow(),
                    symptomRepository.findByName("Difficulty breathing").orElseThrow()
            ), DoctorType.ALLERGY_AND_IMMUNOLOGY, diseaseRepository);
            createDisease("Malaria", "A disease caused by a plasmodium parasite, transmitted by the bite of infected mosquitoes.", "Malaria is a disease caused by a plasmodium parasite, transmitted by the bite of infected mosquitoes.", List.of(
                    symptomRepository.findByName("Fever").orElseThrow(),
                    symptomRepository.findByName("Fatigue").orElseThrow()
            ), DoctorType.GENERAL_PRACTITIONER, diseaseRepository);
            createDisease("Depression", "A mental health disorder characterized by persistently depressed mood or loss of interest in activities, causing significant impairment in daily life.", "Depression is a mental health disorder characterized by persistently depressed mood or loss of interest in activities, causing significant impairment in daily life.", List.of(
                    symptomRepository.findByName("Fatigue").orElseThrow()
            ), DoctorType.PSYCHIATRIST, diseaseRepository);
            // Add these lines in the `runner` method to create the missing diseases

            createDisease("Asthma", "A condition in which your airways narrow and swell and may produce extra mucus.", "Asthma is a condition in which your airways narrow and swell and may produce extra mucus. This can make breathing difficult and trigger coughing, a whistling sound (wheezing) when you breathe out, and shortness of breath.", List.of(
                    symptomRepository.findByName("Difficulty breathing").orElseThrow(),
                    symptomRepository.findByName("Cough").orElseThrow()
            ), DoctorType.PULMONOLOGY, diseaseRepository);

            createDisease("Bronchitis", "An inflammation of the lining of your bronchial tubes.", "Bronchitis is an inflammation of the lining of your bronchial tubes, which carry air to and from your lungs.", List.of(
                    symptomRepository.findByName("Cough").orElseThrow(),
                    symptomRepository.findByName("Fatigue").orElseThrow()
            ), DoctorType.PULMONOLOGY, diseaseRepository);

            createDisease("Diabetes", "A disease that occurs when your blood glucose is too high.", "Diabetes is a disease that occurs when your blood glucose, also called blood sugar, is too high.", List.of(
                    symptomRepository.findByName("Fatigue").orElseThrow()
            ), DoctorType.ENDOCRINOLOGY, diseaseRepository);

            createDisease("Hypertension", "A condition in which the force of the blood against your artery walls is too high.", "Hypertension is a condition in which the force of the blood against your artery walls is high enough that it may eventually cause health problems, such as heart disease.",
                    List.of(),
                    DoctorType.CARDIOLOGY, diseaseRepository);

            createDisease("Hyperthyroidism", "A condition in which the thyroid gland produces too much thyroid hormone.", "Hyperthyroidism occurs when the thyroid gland produces too much thyroxine, accelerating your body's metabolism significantly.", List.of(
                    symptomRepository.findByName("Fatigue").orElseThrow()
            ), DoctorType.ENDOCRINOLOGY, diseaseRepository);

            createDisease("Migraine", "A headache of varying intensity, often accompanied by nausea and sensitivity to light and sound.", "A migraine is a headache of varying intensity, often accompanied by nausea and sensitivity to light and sound.", List.of(
                    symptomRepository.findByName("Headache").orElseThrow()
            ), DoctorType.NEUROLOGY, diseaseRepository);

            createDisease("Osteoporosis", "A condition in which bones become weak and brittle.", "Osteoporosis is a condition in which bones become weak and brittle to the point where a fall or even mild stresses such as bending over can cause a fracture.", List.of(
                    symptomRepository.findByName("Fatigue").orElseThrow()
            ), DoctorType.RHEUMATOLOGY, diseaseRepository);

            createDisease("Pneumonia", "An infection that inflames the air sacs in one or both lungs.", "Pneumonia is an infection that inflames the air sacs in one or both lungs, which may fill with fluid or pus.", List.of(
                    symptomRepository.findByName("Cough").orElseThrow(),
                    symptomRepository.findByName("Fever").orElseThrow(),
                    symptomRepository.findByName("Difficulty breathing").orElseThrow()
            ), DoctorType.PULMONOLOGY, diseaseRepository);

            createDisease("Stroke", "A medical emergency that occurs when blood flow to a part of your brain is interrupted or reduced.", "A stroke occurs when blood flow to a part of your brain is interrupted or reduced, preventing brain tissue from getting oxygen and nutrients.", List.of(
                    symptomRepository.findByName("Headache").orElseThrow(),
                    symptomRepository.findByName("Fatigue").orElseThrow()
            ), DoctorType.NEUROLOGY, diseaseRepository);

        };
    }

    protected void createSymptom(String name, EmergencyType emergencyType,String description, SymptomRepository symptomRepository) {
        Symptom symptom = new Symptom(name, emergencyType, description);

        symptomRepository.findByName(name)
                .ifPresentOrElse(
                        s -> logger.warn("Symptom already exists: " + s.getName()),
                        () -> {
                            logger.info("Inserting symptom: " + symptom.getName());
                            symptomRepository.insert(symptom);
                        }
                );
    }

    protected void createDisease(
            String name,
            String descriptionShort,
            String descriptionLong,
            List<Symptom> symptoms,
            DoctorType doctor,
            DiseaseRepository diseaseRepository
    ) {
        Disease disease = new Disease(name, descriptionShort, descriptionLong, symptoms, doctor);

        diseaseRepository.findDiseaseByName(name)
                .ifPresentOrElse(
                        d -> logger.warn("Disease already exists: " + d.getName()),
                        () -> {
                            logger.info("Inserting disease: " + disease.getName());
                            diseaseRepository.save(disease);
                        }
                );
    }

    protected void createPatient(String username, int age, int height, int weight, PatientRepository patientRepository) {
        Patient patient = new Patient(username, age, height, weight, Gender.MALE, Location.EUROPE, Lifestyle.ACTIVE);
        patientRepository.findByName(patient.getName())
            .ifPresentOrElse(
                u -> logger.warn("User already exists: " + u.getName()),
                () -> {
                    logger.info("Inserting name: " + patient.getName());
                    patientRepository.insert(patient);
                }
            );
    }


//    private static void usingMongoTemplateAndQuery(SymptomRepository symptomRepository, MongoTemplate mongoTemplate, String name, Symptom symptom) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("name").is(name));
//        List<Symptom> symptomList = mongoTemplate.find(query, Symptom.class);
//        if (symptomList.size() > 1) {
//            throw new IllegalStateException("Multiple symptoms with the same name found.");
//        }
//
//        if (symptomList.isEmpty()) {
//            logger.info("Inserting symptom: " + symptom.getName());
//            symptomRepository.insert(symptom);
//        } else {
//            logger.warn("Symptom already exists: " + symptom.getName());
//        }
//    }
}
