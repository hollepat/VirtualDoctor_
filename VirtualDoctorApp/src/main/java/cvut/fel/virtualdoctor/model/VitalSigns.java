package cvut.fel.virtualdoctor.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "vital_signs")
public class VitalSigns {

    @Id
    private String id;

    private LocalDateTime localDateTime;
    private Patient patient;
    private int heartRate;
    private double skinTemperature;
    private double bloodPressure;
    private double bmi;

// not used
//    private double bloodOxygenLevel;
//    private double respiratoryRate;

    public VitalSigns(
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
