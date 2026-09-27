package tp2.ibm.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 30) String username,
        @NotBlank @Size(min = 6, message = "Le mot de passe doit faire au moins 6 caractères") String password,
        @NotBlank @Email String email) {}