package com.escuela.act4.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AlumnoRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede pasar de 100 caracteres")
        String nombre,

        @NotBlank(message = "La matrícula es obligatoria")
        @Size(max = 20, message = "La matrícula no puede pasar de 20 caracteres")
        String matricula,

        @NotNull(message = "El escuelaId es obligatorio")
        @Positive(message = "El escuelaId debe ser un número positivo")
        Long escuelaId
) {
}
