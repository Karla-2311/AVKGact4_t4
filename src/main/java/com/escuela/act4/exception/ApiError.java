package com.escuela.act4.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        Map<String, String> errores
) {
    public static ApiError of(int status, String error, String mensaje) {
        return new ApiError(LocalDateTime.now(), status, error, mensaje, null);
    }

    public static ApiError of(int status, String error, String mensaje, Map<String, String> errores) {
        return new ApiError(LocalDateTime.now(), status, error, mensaje, errores);
    }
}
