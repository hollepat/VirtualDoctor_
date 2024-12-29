package cvut.fel.virtualdoctor.dto;

import java.time.LocalDateTime;

public record HealthDataDTO(
        double skinTemperature,
        double bloodPressure,
        int heartRate,
        LocalDateTime time,
        String name // patient name
) {


}
