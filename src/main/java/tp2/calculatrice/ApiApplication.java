package tp2.calculatrice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.resilience.annotation.EnableResilientMethods;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}
