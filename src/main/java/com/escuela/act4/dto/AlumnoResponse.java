package com.escuela.act4.dto;

public record AlumnoResponse(
        Long id,
        String nombre,
        String matricula,
        EscuelaResponse escuela
) {
}
