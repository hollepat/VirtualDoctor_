package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.HealthData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VitalSignsRepository extends JpaRepository<HealthData, String> {

    List<HealthData> findByPatientName(String username);
}
