package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, String> {

    Optional<Disease> findDiseaseByName(String name);
}
