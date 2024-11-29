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
@Table(name = "patient_input")
public class PatientInput {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // AUTO will let the JPA provider handle UUID generation
    private UUID id;  // Change from Long to UUID

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private LocalDateTime localDateTime;

    @ManyToMany
    @JoinTable(
            name = "patient_input_symptom",
            joinColumns = @JoinColumn(name = "patient_input_id"),
            inverseJoinColumns = @JoinColumn(name = "symptom_id")
    )
    private List<Symptom> symptoms;

    private Double cholesterolLevel;

    public PatientInput(Patient patient, List<Symptom> symptoms, Double cholesterolLevel) {
        this.patient = patient;
        this.localDateTime = LocalDateTime.now();
        this.symptoms = symptoms;
        this.cholesterolLevel = cholesterolLevel;
    }
}