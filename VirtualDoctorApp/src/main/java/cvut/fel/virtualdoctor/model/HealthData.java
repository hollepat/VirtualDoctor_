package cvut.fel.virtualdoctor.model;

import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "vital_signs")
public class HealthData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // AUTO will let the JPA provider handle UUID generation
    private UUID id;

    private LocalDateTime localDateTime;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private int heartRate;
    private double skinTemperature;
    private double bloodPressure;
    private double bmi;

    public HealthData(
            Patient patient,
            LocalDateTime localDateTime,
            double skinTemperature,
            double bloodPressure,
            double bmi,
            int heartRate
    ) {
        this.patient = patient;
        this.localDateTime = localDateTime;
        this.skinTemperature = skinTemperature;
        this.bloodPressure = bloodPressure;
        this.bmi = bmi;
        this.heartRate = heartRate;
    }
}