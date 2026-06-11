package ar.com.rodrifernandez.priceapi.exception;

import org.springframework.http.HttpStatus;

/**
 * Excepción de propósito general para representar fallos de infraestructura
 * o errores que no tienen una categoría de negocio específica.
 *
 * Lleva el HttpStatus como campo propio para que el handler pueda resolver
 * dinámicamente el código de respuesta — evitando crear una subclase por
 * cada código HTTP posible (OCP: abierto para extensión, cerrado para modificación).
 *
 * Ejemplos de uso:
 *   - Base de datos no disponible          → 503 SERVICE_UNAVAILABLE
 *   - Operación no permitida en ese estado → 409 CONFLICT
 *   - Error interno no controlado          → 500 INTERNAL_SERVER_ERROR
 */
public class PriceApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;
	private final HttpStatus httpStatus;

    public PriceApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public PriceApiException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
