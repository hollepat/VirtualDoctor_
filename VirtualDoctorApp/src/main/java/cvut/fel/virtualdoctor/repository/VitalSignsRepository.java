package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.VitalSigns;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VitalSignsRepository extends JpaRepository<VitalSigns, String> {

    List<VitalSigns> findByPatientName(String username); // TODO check if works
}
