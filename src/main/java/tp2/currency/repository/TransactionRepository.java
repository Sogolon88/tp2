package tp2.currency.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import tp2.currency.entity.CurrencyTransaction;


public interface TransactionRepository extends JpaRepository<CurrencyTransaction, Long> {
}