package cvut.fel.virtualdoctor.model;

import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "differential_list")
public class DifferentialList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // AUTO will let the JPA provider handle UUID generation
    private UUID id;

    @ElementCollection
    @CollectionTable(name = "differential_list_ddx", joinColumns = @JoinColumn(name = "differential_list_id"))
    @MapKeyColumn(name = "disease")
    @Column(name = "probability")
    private Map<String, Double> ddx;

    public DifferentialList(Map<String, Double> ddx) {
        this.ddx = ddx;
    }

    @Override
    public String toString() {
        return "DifferentialDiagnosis{" +
                "ddx=" + ddx +
                '}';
    }
}