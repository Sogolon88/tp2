
package tp2.calculatrice.dto;
import jakarta.validation.constraints.*;

public class OperationRequest {
    
    @NotNull(message = "Le champ a est obligatoire")
    private Double a;
    
    @NotNull(message = "Le champ b est obligatoire")
    private Double b;
    
    @NotBlank(message = "L'operation est obligatoire")
    private String operation;

    // getters et setters
    public Double getA() { return a; }
    public void setA(Double a) { this.a = a; }
    public Double getB() { return b; }
    public void setB(Double b) { this.b = b; }
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
}
