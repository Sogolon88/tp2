package tp2.ibm.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tp2.ibm.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);   // utile pour refuser un doublon à l'inscription
}