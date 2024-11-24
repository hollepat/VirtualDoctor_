package cvut.fel.virtualdoctor.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter @Getter
@Document
public class Symptom {

    @Id
    private String id;

    //@Indexed(unique = true)
    private String name;

    private String description;

    private EmergencyType emergency;

    public Symptom(String name, EmergencyType emergency, String description) {
        this.name = name;
        this.emergency = emergency;
        this.description = description;
    }
}
