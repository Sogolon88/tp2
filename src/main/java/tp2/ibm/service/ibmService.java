package tp2.ibm.service;

import org.springframework.stereotype.Service;

import tp2.commonException.ResourceNotFoundException;
import tp2.ibm.entity.BMIRecord;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tp2.ibm.entity.User;
import tp2.ibm.dto.BMIRequest;
import tp2.ibm.dto.BMIStatistics;
import tp2.ibm.repository.BMIRepository;
import tp2.ibm.repository.UserRepository;

@Service 
public class ibmService {
    private  final BMIRepository bmiRepository;
    private  final UserRepository userRepository;

    public ibmService(BMIRepository bmiRepository, UserRepository userRepository){
        this .bmiRepository = bmiRepository;
        this.userRepository = userRepository;
    }
    
    public double calculerBMI(BMIRecord bmiRecord) {
        return bmiRecord.getWeight() / (bmiRecord.getHeight() * bmiRecord.getHeight());
    }

    public List<BMIRecord>  getHistoriqueBMI(User principal) {
        User user = userRepository.findByUsername(principal.getUsername())
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        List<BMIRecord> historique = bmiRepository.findByUserId(user.getId());
      
        return historique;
    }

    public BMIRecord calculerBMI(BMIRequest req) {
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        double bmiValue = req.weight() / (req.height() * req.height());
        BMIRecord bmiRecord = new BMIRecord();
        bmiRecord.setWeight(req.weight());
        bmiRecord.setHeight(req.height());
        bmiRecord.setBmi(bmiValue);
        bmiRecord.setUserId(user.getId());

        return bmiRepository.save(bmiRecord);
    }

    public void save(User user) {
        user.setPassword(user.getPassword());
        userRepository.save(user);
    }

    public BMIStatistics statistiques() {
    List<BMIRecord> records = bmiRepository.findAll();

    // Répartition : on part de 0 pour chaque catégorie, dans l'ordre, pour qu'elles apparaissent toutes
    Map<String, Long> repartition = new LinkedHashMap<>();
    repartition.put("Insuffisance pondérale", 0L);
    repartition.put("Poids normal", 0L);
    repartition.put("Surpoids", 0L);
    repartition.put("Obésité", 0L);
    for (BMIRecord r : records) {
        repartition.merge(categorie(r.getBmi()), 1L, Long::sum);
    }

    // Moyenne arrondie à 1 décimale ; null s'il n'y a encore aucune mesure
    Double moyenne = records.isEmpty() ? null :
            Math.round(records.stream().mapToDouble(BMIRecord::getBmi).average().orElse(0) * 10) / 10.0;

    return new BMIStatistics(records.size(), moyenne, repartition);
    }

    public static String categorie(double bmi) {
        if (bmi < 18.5) return "Insuffisance pondérale";
        if (bmi < 25)   return "Poids normal";
        if (bmi < 30)   return "Surpoids";
        return "Obésité";
    }
}
