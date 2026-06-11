package ar.com.rodrifernandez.priceapi.exception;

import ar.com.rodrifernandez.priceapi.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/**
 * Intercepta TODAS las excepciones no manejadas en los controllers.
 *
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody, por lo que la
 * respuesta JSON se serializa automáticamente sin necesidad de @ResponseBody
 * en cada método.
 *
 * Orden de los handlers: de más específico a más genérico — Spring usa el
 * primer método cuyo tipo de excepción hace match, por lo que el orden importa.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // -------------------------------------------------------------------------
    // 404 NOT FOUND — Recurso inexistente
    // -------------------------------------------------------------------------

    /**
     * Producto no encontrado por id o por criterio (store, type).
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleProductNotFound(
            ProductNotFoundException ex,
            HttpServletRequest request) {

        log.warn("Recurso no encontrado: {} — path: {}", ex.getMessage(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.of(
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    // -------------------------------------------------------------------------
    // 400 BAD REQUEST — Datos inválidos enviados por el cliente
    // -------------------------------------------------------------------------

    /**
     * Captura errores de validación de Bean Validation (@NotNull, @Size, etc.)
     * sobre los campos del ProductRequest.
     * Consolida todos los errores de campo en un único mensaje descriptivo.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> "'" + fe.getField() + "': " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));

        String message = "Datos de entrada inválidos. " + fieldErrors;

        log.warn("Validación fallida en {}: {}", request.getRequestURI(), fieldErrors);

        return ResponseEntity
                .badRequest()
                .body(ApiErrorResponse.of(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        message,
                        request.getRequestURI()
                ));
    }

    /**
     * El cuerpo del request está malformado o es JSON inválido.
     * Ej: se envía un string donde se espera un número en el campo price.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadableMessage(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        log.warn("Cuerpo de request ilegible en {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body(ApiErrorResponse.of(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "El cuerpo del request es inválido o tiene formato incorrecto.",
                        request.getRequestURI()
                ));
    }

    /**
     * Tipo de parámetro de ruta incompatible.
     * Ej: GET /v1/products/abc cuando el id espera un Long.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String message = "El parámetro '" + ex.getName() + "' recibió el valor '" +
                ex.getValue() + "', que no es del tipo esperado.";

        log.warn("Tipo de argumento inválido en {}: {}", request.getRequestURI(), message);

        return ResponseEntity
                .badRequest()
                .body(ApiErrorResponse.of(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        message,
                        request.getRequestURI()
                ));
    }

    // -------------------------------------------------------------------------
    // 503 SERVICE_UNAVAILABLE / código dinámico — Fallo de infraestructura
    // -------------------------------------------------------------------------

    /**
     * Excepción de infraestructura con código HTTP dinámico.
     * El código HTTP se extrae del campo httpStatus del propio objeto de excepción,
     * lo que permite cubrir 503, 409, 500, etc. con un único handler.
     */
    @ExceptionHandler(PriceApiException.class)
    public ResponseEntity<ApiErrorResponse> handlePriceApiException(
            PriceApiException ex,
            HttpServletRequest request) {

        log.error("Error de infraestructura [{}] en {}: {}",
                ex.getHttpStatus(), request.getRequestURI(), ex.getMessage(), ex);

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(ApiErrorResponse.of(
                        ex.getHttpStatus().value(),
                        ex.getHttpStatus().getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    // -------------------------------------------------------------------------
    // 500 INTERNAL SERVER ERROR — Última línea de defensa
    // -------------------------------------------------------------------------

    /**
     * Captura cualquier excepción no prevista.
     * NO expone el mensaje interno (puede contener datos sensibles o stack traces).
     * El mensaje real se loguea internamente para diagnóstico.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Error inesperado en {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        return ResponseEntity
                .internalServerError()
                .body(ApiErrorResponse.of(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        "Ocurrió un error interno inesperado. Por favor, intentá más tarde.",
                        request.getRequestURI()
                ));
    }
}
