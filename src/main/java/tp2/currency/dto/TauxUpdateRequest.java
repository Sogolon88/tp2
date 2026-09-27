package tp2.currency.dto;

import jakarta.validation.constraints.*;

public class TauxUpdateRequest {
    @NotBlank @Size(min = 3, max = 3)
    private String fromCurrency;

    @NotBlank @Size(min = 3, max = 3)
    private String toCurrency;

    @NotNull @Positive(message = "Le taux doit être strictement positif")
    private Double rate;

    // getters et setters
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