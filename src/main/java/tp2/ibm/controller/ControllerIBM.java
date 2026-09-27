package tp2.ibm.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tp2.commonException.ResourceNotFoundException;
import tp2.ibm.entity.BMIRecord;
import tp2.ibm.repository.BMIRepository;
import tp2.ibm.service.ibmService;
import tp2.ibm.repository.UserRepository;
import tp2.ibm.entity.User;
import tp2.ibm.dto.BMIRequest;
import tp2.ibm.dto.BMIStatistics;



@RestController
@RequestMapping("/api/bmi")
public class ControllerIBM {
    
    private  UserRepository userRepository;
    private ibmService bmiService;
    private BMIRepository bmiRepository;

    public ControllerIBM(UserRepository userRepository, ibmService bmiService, BMIRepository bmiRepository) {
        this.userRepository = userRepository;
        this.bmiService = bmiService;
        this.bmiRepository = bmiRepository;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBMIRecord(@PathVariable("id") Long id, Principal principal) {
        BMIRecord bmiRecord = bmiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enregistrement BMI introuvable"));

        bmiRepository.delete(bmiRecord);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
        public BMIStatistics statistiques() {
            return bmiService.statistiques();
        }

    @PutMapping("/update/{id}")
    public ResponseEntity<BMIRecord> updateBMIRecord(@PathVariable("id") Long id, Principal principal) {
        BMIRecord bmiRecord = bmiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enregistrement BMI introuvable"));

        bmiRecord.setWeight(bmiRecord.getWeight());
        bmiRecord.setHeight(bmiRecord.getHeight());
        bmiRecord.setBmi(bmiService.calculerBMI(bmiRecord));
        bmiRepository.save(bmiRecord);

        return ResponseEntity.ok(bmiRecord);
    }

    @GetMapping("/history")
    public List<BMIRecord> historique(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        return bmiRepository.findByUserId(user.getId());
    }


    @PostMapping("/calculate")
    public ResponseEntity<BMIRecord> calculer(@Valid @RequestBody BMIRequest req, Principal principal) {
        BMIRecord bmiRecord = bmiService.calculerBMI(req);

        return ResponseEntity.status(HttpStatus.CREATED).body(bmiRecord);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Erreur: Nom d'utilisateur déjà utilisé!");
        }

        bmiService.save(user);
        return ResponseEntity.ok("Utilisateur enregistré avec succès!");
    }



}
