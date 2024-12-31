package cvut.fel.virtualdoctor.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "patient_data")
public class PatientData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Changed to IDENTITY since we're using SERIAL
    @Column(name = "id", nullable = false)
    private Integer id;

    // Removed quotes and uppercase, using standard column names
    @Column(name = "disease")
    private String disease;

    @Column(name = "fever")
    private String fever;

    @Column(name = "cough")
    private String cough;

    @Column(name = "fatigue")
    private String fatigue;

    @Column(name = "difficulty_breathing") // Using underscore instead of space
    private String difficultyBreathing;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "blood_pressure")
    private String bloodPressure;

    @Column(name = "cholesterol_level")
    private String cholesterolLevel;

    @Column(name = "outcome_variable")
    private String outcomeVariable;

    @Column(name = "headache")
    private String headache;

    @Column(name = "sore_throat")
    private String soreThroat;

    @Column(name = "runny_nose")
    private String runnyNose;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "bmi")
    private Double bmi;

    // Updated the setSymptom method to handle the case-sensitive symptom names
    public void setSymptom(String symptom, boolean contains) {
        String value = contains ? "Yes" : "No";
        // Using lowercase comparison to make the matching more robust
        switch (symptom.toLowerCase()) {
            case "fever":
                setFever(value);
                break;
            case "cough":
                setCough(value);
                break;
            case "fatigue":
                setFatigue(value);
                break;
            case "difficulty breathing":
                setDifficultyBreathing(value);
                break;
            case "headache":
                setHeadache(value);
                break;
            case "sore throat":
                setSoreThroat(value);
                break;
            case "runny nose":
                setRunnyNose(value);
                break;
            default:
                throw new IllegalArgumentException("Unknown symptom: " + symptom);
        }
    }
}