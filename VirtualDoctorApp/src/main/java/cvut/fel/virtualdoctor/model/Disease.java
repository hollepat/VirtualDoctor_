package cvut.fel.virtualdoctor.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Data
@Document(collection = "disease")
public class Disease {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;
    private String descriptionShort;
    private String descriptionLong;
    private List<Symptom> symptoms;

    private DoctorType doctor;

    public Disease(String name, String descriptionShort, String descriptionLong, List<Symptom> symptoms, DoctorType doctor) {
        this.name = name;
        this.descriptionShort = descriptionShort;
        this.descriptionLong = descriptionLong;
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
