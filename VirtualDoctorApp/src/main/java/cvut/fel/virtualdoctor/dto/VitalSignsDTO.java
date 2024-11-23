package cvut.fel.virtualdoctor.dto;

import java.time.LocalDateTime;

public record VitalSignsDTO(
//        String temperature,
//        String bloodPressure,
        String heartRate,
//        String respiratoryRate,
//        String oxygenSaturation,
        LocalDateTime date
) {


}
