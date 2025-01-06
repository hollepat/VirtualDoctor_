package cvut.fel.virtualdoctor.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "disease_details")
public class DiseaseDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String description;

    @ManyToMany
    @JoinTable(
            name = "disease_details_medications",
            joinColumns = @JoinColumn(name = "details_id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id")
    )
    private List<Medication> medications; // List of medications

    @ElementCollection
    @CollectionTable(name = "disease_treatments", joinColumns = @JoinColumn(name = "details_id"))
    @Column(name = "treatment")
    private List<String> treatments; // Non-medication treatments like therapy

    @ElementCollection
    @CollectionTable(name = "disease_precautions", joinColumns = @JoinColumn(name = "details_id"))
    @Column(name = "precaution")
    private List<String> precautions; // Preventive measures or precautions

    public DiseaseDetails(String description, List<Medication> medications, List<String> treatments, List<String> precautions) {
        this.description = description;
        this.medications = medications;
        this.treatments = treatments;
        this.precautions = precautions;
    }
}