package tp2.currency.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tp2.currency.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import tp2.commonException.ResourceNotFoundException;
import tp2.currency.client.TauxClient;
import tp2.currency.dto.ConversionManuelleRequest;
import tp2.currency.dto.TauxReponse;
import tp2.currency.dto.TauxUpdateRequest;
import tp2.currency.entity.CurrencyTransaction;
import tp2.currency.entity.ExchangeRate;
import tp2.currency.repository.RateRepository;

@Service
public class CurrencyService {

    private final WebClient webClient = WebClient.create("https://api.frankfurter.app");
    private final TransactionRepository transactionRepository;
    private final RateRepository rateRepository;

    public CurrencyService(TransactionRepository transactionRepository, RateRepository rateRepository) {
        this.transactionRepository = transactionRepository;
        this.rateRepository = rateRepository;
    }
    
    public double getTaux(String from, String to) {
        TauxReponse  reponse = webClient.get()
        .uri("/latest?from={from}&to={to}", from, to)
        .retrieve()
        .bodyToMono(TauxReponse.class)
        .block(); // .block() attend la réponse (utilisation simple, synchrone)

        return reponse.getRates().get(to);
    }

    @Transactional
    public CurrencyTransaction convertirEtEnregistrer(Double amount, String from, String to) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Le montant doit être strictement positif");
        }
        from = from.toUpperCase();
        to = to.toUpperCase();

        TauxClient tauxClient = new TauxClient();
        double taux = tauxClient.getTaux(from, to);   // appel externe (voir remarque ci-dessous)

        CurrencyTransaction t = new CurrencyTransaction();
        t.setAmount(amount);
        t.setFromCurrency(from);
        t.setToCurrency(to);
        t.setRate(taux);
        t.setResult(amount * taux);
        t.setDate(LocalDateTime.now());

        return transactionRepository.save(t);
    }

    @Transactional
    public CurrencyTransaction updateTransaction(Long id, ConversionManuelleRequest req) {
        CurrencyTransaction existingTx = transactionRepository.findById(id).orElse(null);
        if (existingTx == null) {
            return null; // Transaction non trouvée
        }

        String from = req.getFromCurrency().toUpperCase();
        String to = req.getToCurrency().toUpperCase();

        // Met à jour le taux manuel dans la table des taux
        ExchangeRate taux = rateRepository.findByFromCurrencyAndToCurrency(from, to)
                .orElseGet(ExchangeRate::new);
        taux.setFromCurrency(from);
        taux.setToCurrency(to);
        taux.setRate(req.getRate());
        taux.setUpdatedAt(LocalDateTime.now());
        rateRepository.save(taux);

        // Met à jour la transaction existante
        existingTx.setAmount(req.getAmount());
        existingTx.setFromCurrency(from);
        existingTx.setToCurrency(to);
        existingTx.setRate(req.getRate());
        existingTx.setResult(req.getAmount() * req.getRate());
        existingTx.setDate(LocalDateTime.now());

        return transactionRepository.save(existingTx);
    }

    @Transactional
    public CurrencyTransaction convertirAvecTauxManuel(ConversionManuelleRequest req) {
        String from = req.getFromCurrency().toUpperCase();
        String to = req.getToCurrency().toUpperCase();

        // 1) Enregistre le taux manuel, ou le met à jour s'il existe déjà pour cette paire
        ExchangeRate taux = rateRepository.findByFromCurrencyAndToCurrency(from, to)
                .orElseGet(ExchangeRate::new);
        taux.setFromCurrency(from);
        taux.setToCurrency(to);
        taux.setRate(req.getRate());
        taux.setUpdatedAt(LocalDateTime.now());
        rateRepository.save(taux);

        // 2) Conversion et enregistrement de la transaction
        CurrencyTransaction t = new CurrencyTransaction();
        t.setAmount(req.getAmount());
        t.setFromCurrency(from);
        t.setToCurrency(to);
        t.setRate(req.getRate());
        t.setResult(req.getAmount() * req.getRate());
        t.setSource("MANUEL");
        t.setDate(LocalDateTime.now());

        return transactionRepository.save(t);
    }

    @Transactional
    public ExchangeRate mettreAJourTaux(TauxUpdateRequest req) {
        String from = req.getFromCurrency().toUpperCase();
        String to = req.getToCurrency().toUpperCase();

        ExchangeRate taux = rateRepository.findByFromCurrencyAndToCurrency(from, to)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aucun taux enregistré pour " + from + " → " + to));

        taux.setRate(req.getRate());
        taux.setUpdatedAt(LocalDateTime.now());
        return rateRepository.save(taux);
    }

    @Transactional
    public void supprimerTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction avec l'ID " + id + " non trouvée");
        }
        transactionRepository.deleteById(id);
    }
}
