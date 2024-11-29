package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SymptomRepository extends JpaRepository<Symptom, String> {

    Optional<Symptom> findByName(String name);

   /* this way we can write mongodb queries in the repository interface
   @Query("{ 'name' : ?0 }")
   void TestMethod();
    */
}
