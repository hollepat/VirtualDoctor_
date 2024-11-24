package cvut.fel.virtualdoctor.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "patient")
public class Patient {

    @Id
    private String id;

    //@Indexed(unique = true) TODO don't know how to handle this
    private String name;

    // metadata about User
    private int age;
    private int height; // in cm
    private int weight; // in kg
    private Gender gender;
    private Location location;
    private Lifestyle lifestyle;
    private List<Diagnosis> historyOfDiagnoses;

    public Patient(String name, int age, int height, int weight, Gender gender, Location location, Lifestyle lifestyle) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    public double getBmi() {
        double height_in_m = (double) height / 100;
        return (double) weight / (height_in_m * height_in_m);
    }
}
