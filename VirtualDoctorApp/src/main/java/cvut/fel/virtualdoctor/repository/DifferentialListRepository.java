package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.DifferentialList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DifferentialListRepository extends JpaRepository<DifferentialList, String> {
}
