package tp2.calculatrice.controller;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import tp2.calculatrice.repository.OperationRepository;
import tp2.commonException.DivisionParZeroException;
import tp2.calculatrice.exception.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import tp2.calculatrice.entity.Operation;
import tp2.calculatrice.dto.*;
import tp2.calculatrice.controller.*;

@RestController
@RequestMapping("/api/calculator")
public class CalculatriceController{

    private final OperationRepository repository;

    public CalculatriceController(OperationRepository repository){
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Operation> calculer(@RequestBody @Valid OperationRequest req){
        double resultat = switch (req.getOperation()){
            case "addition" -> req.getA() + req.getB();
            case "soustraction" -> req.getA() - req.getB();
            case "multiplication" -> req.getA() * req.getB();
            case "division" -> {

                if (req.getB() == 0) {
                    throw new DivisionParZeroException("Division par zéro interdite");
                }
                yield req.getA() / req.getB();
                }

            default -> throw new IllegalArgumentException("Opération inconnue");
        };

            Operation op = new Operation();
            op.setOperateur(req.getOperation());
            op.setA(req.getA());
            op.setB(req.getB());
            op.setResultat(resultat);
            op.setDate(LocalDateTime.now());

            repository.save(op);
            return ResponseEntity.status(HttpStatus.CREATED).body(op);
    }

    @GetMapping("/history")
    public Page<Operation> historique(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Operation> modifier(@PathVariable("id") Long id, @RequestBody OperationRequest maj) {
    Operation op = repository.findById(id)
    .orElseThrow(() -> new RuntimeException("Opération introuvable"));
    op.setA(maj.getA());
    op.setB(maj.getB());
    op.setOperateur(maj.getOperation());
    switch (maj.getOperation()) {
        case "addition":
            op.setResultat(op.getA() + op.getB());
            break;
        case "soustraction":
            op.setResultat(op.getA() - op.getB());
            break;
        case "multiplication":
            op.setResultat(op.getA() * op.getB());
            break;
        case "division":
            if (op.getB() == 0) {
                throw new DivisionParZeroException("Division par zéro interdite");
            }
            op.setResultat(op.getA() / op.getB());
            break;
        default:
            throw new IllegalArgumentException("Opération inconnue");
    }
    repository.save(op);
    return ResponseEntity.ok(op);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable("id") Long id) {
    repository.deleteById(id);
    return ResponseEntity.noContent().build();
    }

}

