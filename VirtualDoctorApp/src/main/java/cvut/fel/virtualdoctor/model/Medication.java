package cvut.fel.virtualdoctor.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "medication")
public class Medication {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)  // AUTO will let the JPA provider handle UUID generation
        private UUID id;

        private String name;
        private String description;
        private String dosage;
        private String sideEffects;

        public Medication(String name, String description, String dosage, String sideEffects) {
            this.name = name;
            this.description = description;
            this.dosage = dosage;
            this.sideEffects = sideEffects;
        }
}
