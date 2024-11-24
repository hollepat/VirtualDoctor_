package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PatientRepository extends MongoRepository<Patient, String>{

    Optional<Patient> findByName(String name);
}
