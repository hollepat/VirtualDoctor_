package cvut.fel.virtualdoctor.dto;

import cvut.fel.virtualdoctor.model.DoctorType;
import cvut.fel.virtualdoctor.model.EmergencyType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record DiagnosisDTO(
        String swVersion,
        LocalDateTime timeAndDate,
        List<DoctorType> doctorsToVisit,
        Map<String, Double> diseases,
        EmergencyType emergency
) {
}
