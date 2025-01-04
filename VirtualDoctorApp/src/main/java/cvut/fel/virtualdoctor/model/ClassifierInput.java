package cvut.fel.virtualdoctor.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "classifier_input")
public class ClassifierInput {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Lifestyle lifestyle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private Location location;

//    @Type(JsonBinaryType.class)
//    @Column(nullable = false, columnDefinition = "jsonb")
    @Column(name = "symptoms", nullable = false)
    private String symptoms; // Stored as JSONB // TODO should be a hash map and conversion handled in postgreSQL, I get unknow error when trying to use jsonb

    @Column(name = "cholesterol_level", nullable = false)
    private double cholesterolLevel;

//    @Type(JsonBinaryType.class)
//    @Column(name = "health_data", nullable = false, columnDefinition = "jsonb")
    @Column(name = "health_data", nullable = false)
    private String healthData; // Stored as JSONB // TODO should be a hash map and conversion handled in postgreSQL, I get unknow error when trying to use jsonb

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_input_id", nullable = false)
    private PatientInput patientInput;

    public ClassifierInput(int age, Lifestyle lifestyle, Gender gender, Location location, String symptoms, double cholesterolLevel, String healthData, PatientInput patientInput) {
        this.age = age;
        this.lifestyle = lifestyle;
        this.gender = gender;
        this.location = location;
        this.symptoms = symptoms;
        this.cholesterolLevel = cholesterolLevel;
        this.healthData = healthData;
        this.patientInput = patientInput;
    }

    public Map<String, Double> getHealthDataAsMap() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(healthData, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse health data JSON", e);
        }
    }

    public void setHealthDataFromMap(Map<String, Double> healthDataMap) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.healthData = mapper.writeValueAsString(healthDataMap);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize health data to JSON", e);
        }
    }

    public List<String> getSymptomsAsList() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(symptoms, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse symptoms JSON", e);
        }
    }

    public void setSymptomsFromList(List<String> symptomsList) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.symptoms = mapper.writeValueAsString(symptomsList);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize symptoms to JSON", e);
        }
    }
}