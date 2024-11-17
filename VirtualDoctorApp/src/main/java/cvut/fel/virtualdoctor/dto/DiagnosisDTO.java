package cvut.fel.virtualdoctor.dto;

import cvut.fel.virtualdoctor.model.DoctorType;
import cvut.fel.virtualdoctor.model.EmergencyType;

import java.time.LocalDateTime;
import java.util.Map;

public record DiagnosisDTO(
        String swVersion,
        LocalDateTime timeAndDate,
        DoctorType doctorToVisit,
        Map<String, Double> diseases,
        EmergencyType emergency
) {
}
