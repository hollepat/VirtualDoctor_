package cvut.fel.virtualdoctor.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document(collection = "differential_list")
public class DifferentialList {

    @Id
    private String id;

    private Map<String, Double> ddx;   // diseases and its probabilities

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
