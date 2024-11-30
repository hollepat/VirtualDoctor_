package cvut.fel.virtualdoctor.model;

import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "diagnosis")
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // AUTO will let the JPA provider handle UUID generation
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "patient_input_id", referencedColumnName = "id")
    private PatientInput patientInput;

    @ManyToOne
    private Patient patient;

    private String swVersion;
    private LocalDateTime timeAndDate;

    @ManyToOne
    @JoinColumn(name = "differential_list_id")
    private DifferentialList differentialList;

    @Enumerated(EnumType.STRING)
    private EmergencyType emergency;

    @ElementCollection(targetClass = DoctorType.class)
    @CollectionTable(name = "diagnosis_doctor", joinColumns = @JoinColumn(name = "diagnosis_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "doctor")
    private List<DoctorType> doctorsToVisit;

    public Diagnosis(
            String swVersion,
            LocalDateTime timeAndDate,
            DifferentialList differentialList,
            List<DoctorType> doctorsToVisit,
            EmergencyType emergency,
            Patient patient,
            PatientInput patientInput
    ) {
        this.swVersion = swVersion;
        this.timeAndDate = timeAndDate;
        this.differentialList = differentialList;
        this.doctorsToVisit = doctorsToVisit;
        this.emergency = emergency;
        this.patient = patient;
        this.patientInput = patientInput;
    }

    @Override
    public String toString() {
        return "Diagnosis{" +
                "timeAndDate=" + timeAndDate +
                ", differentialDiagnosis=" + differentialList +
                ", emergency=" + emergency +
                ", doctorsToVisit=" + doctorsToVisit +
                '}';
    }
}