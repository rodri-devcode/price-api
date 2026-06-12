package ar.com.rodrifernandez.priceapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Estructura estándar de error devuelta por la API")
public record ApiErrorResponse(

        @Schema(description = "Momento exacto del error", example = "2025-06-11T14:30:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,

        @Schema(description = "Código de estado HTTP", example = "404")
        int status,

        @Schema(description = "Nombre corto del error HTTP", example = "Not Found")
        String error,

        @Schema(description = "Mensaje amigable para el consumidor de la API", example = "El producto con id 99 no fue encontrado.")
        String message,

        @Schema(description = "URI que originó el error", example = "/v1/products/99")
        String path
) {
    public static ApiErrorResponse of(int status, String error, String message, String path) {
        return new ApiErrorResponse(LocalDateTime.now(), status, error, message, path);
    }
}