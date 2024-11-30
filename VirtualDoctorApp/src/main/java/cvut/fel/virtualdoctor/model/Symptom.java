package cvut.fel.virtualdoctor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "symptom")
public class Symptom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // AUTO will let the JPA provider handle UUID generation
    private UUID id;

    @Column(unique = true)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private EmergencyType emergency;

    public Symptom(String name, EmergencyType emergency, String description) {
        this.name = name;
        this.emergency = emergency;
        this.description = description;
    }
}