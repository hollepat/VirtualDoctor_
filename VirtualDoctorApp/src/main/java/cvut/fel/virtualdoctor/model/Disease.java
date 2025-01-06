package cvut.fel.virtualdoctor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "disease")
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // AUTO will let the JPA provider handle UUID generation
    private UUID id;

    @Column(unique = true)
    private String name;
    private String descriptionShort;

    @OneToOne(cascade = CascadeType.ALL) // Cascade to allow saving details together with disease
    @JoinColumn(name = "details_id")
    private DiseaseDetails details;

    @ManyToMany
    @JoinTable(
            name = "disease_symptom",
            joinColumns = @JoinColumn(name = "disease_id"),
            inverseJoinColumns = @JoinColumn(name = "symptom_id")
    )
    private List<Symptom> symptoms;

    @Enumerated(EnumType.STRING)
    private DoctorType doctor;

    public Disease(String name, String descriptionShort, String descriptionLong, List<Symptom> symptoms, DoctorType doctor) {
        this.name = name;
        this.descriptionShort = descriptionShort;
        this.symptoms = symptoms;
        this.doctor = doctor;
    }

    // Ensure proper comparison and hashing for use in a Map
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disease disease = (Disease) o;
        return Objects.equals(name, disease.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Disease{" +
                "name='" + name + '\'' +
                '}';
    }
}