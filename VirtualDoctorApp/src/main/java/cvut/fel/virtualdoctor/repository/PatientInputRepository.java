package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.PatientInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientInputRepository extends JpaRepository<PatientInput, UUID> {
}
