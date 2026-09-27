package tp2.ibm;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@EnableResilientMethods
public class AppApplicationIBM {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(AppApplicationIBM.class, args);
    }
}