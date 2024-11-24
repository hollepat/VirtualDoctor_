package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.PatientInput;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatientInputRepository extends MongoRepository<PatientInput, String> {
}
