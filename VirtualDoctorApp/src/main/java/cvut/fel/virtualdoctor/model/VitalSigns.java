package cvut.fel.virtualdoctor.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "vital_signs")
public class VitalSigns {

    // id will be a timestamp which will identify also the time it was taken
    @Id
    private String id;

    private LocalDateTime localDateTime;
    private User user;
    private int heartRate;
    private double temperature;
    private double bloodPressure;
    private double cholesterolLevel;
    private double bmi;

// not used
//    private double bloodOxygenLevel;
//    private double respiratoryRate;

    public VitalSigns(
        User user,
        LocalDateTime localDateTime,
        double temperature, // TODO rename to skinTemperature
        double bloodPressure,
        double bmi,
        double cholesterolLevel,
        int heartRate
    ) {
        this.user = user;
        this.localDateTime = localDateTime;
        this.temperature = temperature;
        this.bloodPressure = bloodPressure;
        this.cholesterolLevel = cholesterolLevel;
        this.bmi = bmi;
        this.heartRate = heartRate;
    }
}
