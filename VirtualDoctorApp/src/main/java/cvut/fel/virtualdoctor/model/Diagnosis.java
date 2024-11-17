package cvut.fel.virtualdoctor.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "diagnosis")
public class Diagnosis {

    @Id
    private String id;

    private String swVersion;
    private LocalDateTime timeAndDate;

    private DifferentialList differentialList;

    // emergency is a flag that indicates if the patient should be seen immediately
    private EmergencyType emergency;

    // doctorToVisit is a flag that indicates which doctor should the patient visit
    private DoctorType doctorToVisit;

    public Diagnosis(
        String swVersion,
        LocalDateTime timeAndDate,
        DifferentialList differentialList,
        DoctorType doctorToVisit,
        EmergencyType emergency
    ) {
        this.swVersion = swVersion;
        this.timeAndDate = timeAndDate;
        this.differentialList = differentialList;
        this.doctorToVisit = doctorToVisit;
        this.emergency = emergency;
    }

    @Override
    public String toString() {
        return "Diagnosis{" +
                "timeAndDate=" + timeAndDate +
                ", differentialDiagnosis=" + differentialList +
                ", emergency=" + emergency +
                ", doctorToVisit=" + doctorToVisit +
                '}';
    }
}
