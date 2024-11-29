package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, String> {
}
