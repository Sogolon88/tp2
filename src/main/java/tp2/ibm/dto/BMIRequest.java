package tp2.ibm.dto;

import jakarta.validation.constraints.*;  

public record BMIRequest(
        @NotNull(message = "Le poids ne peut pas être nul") @Positive(message = "Le poids doit être un nombre positif") Double weight,
        @NotNull(message = "La taille ne peut pas être nulle") @Positive(message = "La taille doit être un nombre positif") Double height,
        @NotNull(message = "L'ID utilisateur est requis") Long userId) {}
