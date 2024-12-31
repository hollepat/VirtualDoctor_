package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.PatientData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatientDataRepository extends JpaRepository<PatientData, UUID> {
}
