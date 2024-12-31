package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.ClassifierInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClassifierInputRepository extends JpaRepository<ClassifierInput, UUID> {
}