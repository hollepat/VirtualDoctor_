package cvut.fel.virtualdoctor.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "diagnosis")
public class Diagnosis {

    @Id
    private String id;

    private UserInput userInput;
    private String swVersion;
    private LocalDateTime timeAndDate;

    private DifferentialList differentialList;

    // emergency is a flag that indicates if the patient should be seen immediately
    private EmergencyType emergency;

    // doctorToVisit is a flag that indicates which doctor should the patient visit
    private List<DoctorType> doctorsToVisit;

    public Diagnosis(
        String swVersion,
        LocalDateTime timeAndDate,
        DifferentialList differentialList,
        List<DoctorType> doctorToVisit,
        EmergencyType emergency
    ) {
        this.swVersion = swVersion;
        this.timeAndDate = timeAndDate;
        this.differentialList = differentialList;
        this.doctorsToVisit = doctorToVisit;
        this.emergency = emergency;
    }

    @Override
    public String toString() {
        return "Diagnosis{" +
                "timeAndDate=" + timeAndDate +
                ", differentialDiagnosis=" + differentialList +
                ", emergency=" + emergency +
                ", doctorToVisit=" + doctorsToVisit +
                '}';
    }
}
