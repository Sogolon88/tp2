package tp2.currency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@EnableCaching 
@EnableResilientMethods
public class ApiCurrencyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCurrencyApplication.class, args);
	}
}