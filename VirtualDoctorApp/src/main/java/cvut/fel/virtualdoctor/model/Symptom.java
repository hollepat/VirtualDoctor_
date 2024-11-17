package cvut.fel.virtualdoctor.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter @Getter
@Document
public class Symptom {

    @Id
    private String id;

    //@Indexed(unique = true)
    private String name;

    private String description;

    public Symptom(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
