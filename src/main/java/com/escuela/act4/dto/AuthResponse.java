package com.escuela.act4.dto;

// Nunca incluye el password del usuario
public record AuthResponse(
        String token,
        String tipo,
        long expiraEnMs,
        String nombre,
        String email
) {
}
