package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.HealthData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HealthDataRepository extends JpaRepository<HealthData, String> {

    List<HealthData> findByPatientName(String username);
}
