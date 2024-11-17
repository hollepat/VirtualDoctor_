package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.Diagnosis;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiagnosisRepository extends MongoRepository<Diagnosis, String>{
}
