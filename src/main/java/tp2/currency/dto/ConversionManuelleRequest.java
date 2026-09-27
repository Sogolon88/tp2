package tp2.currency.dto;

import jakarta.validation.constraints.*;

public class ConversionManuelleRequest {
    @NotNull @Positive
    private Double amount;

    @NotBlank @Size(min = 3, max = 3, message = "Code devise sur 3 lettres (ex. EUR)")
    private String fromCurrency;

    @NotBlank @Size(min = 3, max = 3, message = "Code devise sur 3 lettres (ex. XOF)")
    private String toCurrency;

    @NotNull @Positive(message = "Le taux doit être strictement positif")
    private Double rate;

    // getters et setters
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getFromCurrency() {
        return fromCurrency;
    }
    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }
    public String getToCurrency() {
        return toCurrency;
    }
    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }
    public Double getRate() {
        return rate;
    }
    public void setRate(Double rate) {
        this.rate = rate;
    }
    
}