package tp2.calculatrice.repository;
import tp2.calculatrice.entity.*;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;


public interface OperationRepository extends JpaRepository<Operation, Long> {
   // Page<Operation> findAllPage(Pageable peageable);

}