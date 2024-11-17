package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.Symptom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SymptomRepository extends MongoRepository<Symptom, String> {

    Optional<Symptom> findByName(String name);

   /* this way we can write mongodb queries in the repository interface
   @Query("{ 'name' : ?0 }")
   void TestMethod();
    */
}
