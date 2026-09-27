package tp2.ibm.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tp2.ibm.entity.BMIRecord;

public interface BMIRepository extends JpaRepository<BMIRecord, Long> {
    List<BMIRecord> findByUserId(Long userId);   // historique d'un utilisateur
}