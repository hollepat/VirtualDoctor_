package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.Disease;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DiseaseRepository extends MongoRepository<Disease, String> {

    Optional<Disease> findDiseaseByName(String name);
}
