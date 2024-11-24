package cvut.fel.virtualdoctor.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "patient_input")
public class PatientInput {

    @Id
    private String id;

    @DBRef
    private Patient patient;
    private LocalDateTime localDateTime;
    private List<Symptom> symptoms;

    /**
     * It should be average value of the last measurements.
     * This is optional, as the name may not have the necessary equipment to measure these.
     * If the name does have the equipment, they can provide the data.
     * <br>
     * <br>
     * NOTE: In case of taking data from a device that measures vital signs. Shouldn't be a different
     * type on input??? Which would take periodically the data from the device and store it in the
     * database. Once the name inputs the data, the system should take the last data from the device.
     */
    private VitalSigns vitalSigns;

    public PatientInput(Patient patient, List<Symptom> symptoms, VitalSigns vitalSigns) {
        this.patient = patient;
        this.localDateTime = LocalDateTime.now();
        this.symptoms = symptoms;
        this.vitalSigns = vitalSigns;
    }
}
