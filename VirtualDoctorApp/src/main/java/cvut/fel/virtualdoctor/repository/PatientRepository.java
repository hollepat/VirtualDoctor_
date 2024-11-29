package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, String> {

    Optional<Patient> findByName(String name);
}
