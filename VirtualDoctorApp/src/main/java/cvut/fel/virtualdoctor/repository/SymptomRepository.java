package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SymptomRepository extends JpaRepository<Symptom, String> {
    Optional<Symptom> findByName(String name);
}
