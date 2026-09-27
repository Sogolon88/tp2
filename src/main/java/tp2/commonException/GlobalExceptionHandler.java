package tp2.commonException;

import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return build(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(RuntimeException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }


    @ExceptionHandler (DivisionParZeroException.class)
    public ResponseEntity<Map<String, Object>> handleDivisionParZero(DivisionParZeroException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

        @ExceptionHandler(IdentifiantsInvalidesException.class)
    public ResponseEntity<Map<String, Object>> handleIdentifiants(IdentifiantsInvalidesException ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(ConflitException.class)
    public ResponseEntity<Map<String, Object>> handleConflit(ConflitException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", "/api/calculator");

        return ResponseEntity.status(status).body(body);
        }
    }