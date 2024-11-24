package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.VitalSigns;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VitalSignsRepository extends MongoRepository<VitalSigns, String> {

    @Query("{ 'patient.name' : ?0 }") // ?0 refers to the first parameter
    List<VitalSigns> findByName(String username);
}
