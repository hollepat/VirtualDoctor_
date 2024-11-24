package cvut.fel.virtualdoctor.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "user")
public class User {

    @Id
    private String id;

    //@Indexed(unique = true)
    private String username;

    // metadata about User
    private int age;
    private int height; // in cm
    private int weight; // in kg
    private Gender gender;
    private Location location;
    private Lifestyle lifestyle;
    private List<Diagnosis> historyOfDiagnoses;

    public User(String username, int age, int height, int weight, Gender gender, Location location, Lifestyle lifestyle) {
        this.username = username;
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
