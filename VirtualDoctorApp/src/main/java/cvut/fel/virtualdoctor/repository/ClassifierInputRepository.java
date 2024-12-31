package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.ClassifierInputEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClassifierInputRepository extends JpaRepository<ClassifierInputEntity, UUID> {
}