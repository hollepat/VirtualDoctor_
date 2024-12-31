package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.PatientData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientDataRepository extends JpaRepository<PatientData, UUID> {
}
