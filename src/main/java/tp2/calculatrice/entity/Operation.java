
package tp2.calculatrice.entity;

import jakarta.persistence.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import java.lang.annotation.Inherited;
import org.antlr.v4.runtime.atn.SemanticContext.Operator;

@Entity
public class Operation{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double resultat;
    private double a;
    private double b;
    private LocalDateTime date;
    private String operateur;

    public Operation(){}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOperateur() { return operateur; }
    public void setOperateur(String operateur) { this.operateur = operateur; }
    public double getA() { return a; }
    public void setA(double a) { this.a = a; }
    public double getB() { return b; }
    public void setB(double b) { this.b = b; }
    public double getResultat() { return resultat; }
    public void setResultat(double resultat) { this.resultat = resultat; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }


}