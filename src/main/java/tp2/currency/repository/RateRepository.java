package tp2.currency.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import tp2.currency.entity.ExchangeRate;

public interface RateRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findByFromCurrencyAndToCurrency(String fromCurrency, String toCurrency);
}
