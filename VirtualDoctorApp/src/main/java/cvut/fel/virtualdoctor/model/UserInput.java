package cvut.fel.virtualdoctor.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "user_input")
public class UserInput {

    @Id
    private String id;

    @DBRef
    private User user;
    private LocalDateTime localDateTime;
    private List<Symptom> symptoms;

    /**
     * It should be average value of the last measurements.
     * This is optional, as the user may not have the necessary equipment to measure these.
     * If the user does have the equipment, they can provide the data.
     * <br>
     * <br>
     * NOTE: In case of taking data from a device that measures vital signs. Shouldn't be a different
     * type on input??? Which would take periodically the data from the device and store it in the
     * database. Once the user inputs the data, the system should take the last data from the device.
     */
    private VitalSigns vitalSigns;

    public UserInput(User user, List<Symptom> symptoms, VitalSigns vitalSigns) {
        this.user = user;
        this.localDateTime = LocalDateTime.now();
        this.symptoms = symptoms;
        this.vitalSigns = vitalSigns;
    }
}
