package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.PatientInput;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientInputRepository extends JpaRepository<PatientInput, String> {
}
