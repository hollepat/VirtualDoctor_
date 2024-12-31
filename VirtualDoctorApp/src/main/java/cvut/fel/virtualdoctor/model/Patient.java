package cvut.fel.virtualdoctor.model;

import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // AUTO will let the JPA provider handle UUID generation
    private UUID id;

    @Column(unique = true)
    private String name;

    private int age;
    private int height;
    private int weight;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Location location;

    @Enumerated(EnumType.STRING)
    private Lifestyle lifestyle;

//    @OneToMany(mappedBy = "patient") // TODO fix mapping to have this attribute - not used anyway
//    private List<Diagnosis> historyOfDiagnoses;


    public Patient(String name, int age, int height, int weight, Gender gender, Location location, Lifestyle lifestyle) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.location = location;
        this.lifestyle = lifestyle;
    }

    public double getBmi() {
        double height_in_m = (double) height / 100;
        return (double) weight / (height_in_m * height_in_m);
    }
}