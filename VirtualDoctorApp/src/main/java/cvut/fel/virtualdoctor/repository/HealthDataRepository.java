package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.HealthData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface HealthDataRepository extends JpaRepository<HealthData, String> {

    List<HealthData> findByPatientName(String username);

    @Query("SELECT h FROM HealthData h WHERE h.localDateTime = (SELECT MIN(h2.localDateTime) FROM HealthData h2 WHERE h2.localDateTime > :localDateTime)")
    HealthData findByTimestampClosestTo(LocalDateTime localDateTime);
}
