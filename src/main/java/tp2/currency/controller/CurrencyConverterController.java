package tp2.currency.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tp2.currency.dto.ConversionManuelleRequest;
import tp2.currency.dto.TauxUpdateRequest;
import tp2.currency.entity.CurrencyTransaction;
import tp2.currency.entity.ExchangeRate;
import tp2.currency.service.CurrencyService;

@RestController
@RequestMapping("/api/currency")
public class CurrencyConverterController {

    private final CurrencyService currencyService;

    public CurrencyConverterController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/convert")
    public CurrencyTransaction convert(@RequestParam("amount") Double amount,
                                       @RequestParam("from") String from,
                                       @RequestParam("to") String to) {
        return currencyService.convertirEtEnregistrer(amount, from, to);
    }

    @PostMapping("/convert")
    public ResponseEntity<CurrencyTransaction> convertirAvecTauxManuel(@Valid @RequestBody ConversionManuelleRequest req) {
        CurrencyTransaction tx = currencyService.convertirAvecTauxManuel(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(tx.getId() != null ? tx : null);
    }

    @PutMapping("/update-rate")
    public ExchangeRate mettreAJourTaux(@Valid @RequestBody TauxUpdateRequest req) {
        return currencyService.mettreAJourTaux(req);
    }

    @DeleteMapping("/delete-transaction/{id}")
    public ResponseEntity<Void> supprimerTransaction(@PathVariable("id") Long id) {
        currencyService.supprimerTransaction(id);
        return ResponseEntity.noContent().build();
    }
}