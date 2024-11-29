package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.Disease;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiseaseRepository extends JpaRepository<Disease, String> {

    Optional<Disease> findDiseaseByName(String name);
}
