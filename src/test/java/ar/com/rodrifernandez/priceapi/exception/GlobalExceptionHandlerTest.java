package ar.com.rodrifernandez.priceapi.exception;

import ar.com.rodrifernandez.priceapi.controller.ProductController;
import ar.com.rodrifernandez.priceapi.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ProductController controller = new ProductController(productService);
        // Se registra el GlobalExceptionHandler en el contexto standalone de prueba
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // -------------------------------------------------------------------------
    // 404 — ProductNotFoundException
    // -------------------------------------------------------------------------

    @Test
    void getById_productNotFound_returns404WithBody() throws Exception {
        when(productService.getById(99L)).thenThrow(new ProductNotFoundException(99L));

        mockMvc.perform(get("/v1/products/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("El producto con id 99 no fue encontrado."))
                .andExpect(jsonPath("$.path").value("/v1/products/99"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    // -------------------------------------------------------------------------
    // 400 — Tipo de parámetro de ruta incorrecto
    // -------------------------------------------------------------------------

    @Test
    void getById_invalidIdType_returns400() throws Exception {
        // "abc" no puede convertirse a Long → MethodArgumentTypeMismatchException
        mockMvc.perform(get("/v1/products/abc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(
                        org.hamcrest.Matchers.containsString("id")))
                .andExpect(jsonPath("$.path").value("/v1/products/abc"));
    }

    // -------------------------------------------------------------------------
    // 503 — PriceApiException con código dinámico
    // -------------------------------------------------------------------------

    @Test
    void getAll_infrastructureFailure_returns503() throws Exception {
        when(productService.getAll()).thenThrow(
                new PriceApiException("Base de datos no disponible.", HttpStatus.SERVICE_UNAVAILABLE));

        mockMvc.perform(get("/v1/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503))
                .andExpect(jsonPath("$.error").value("Service Unavailable"))
                .andExpect(jsonPath("$.message").value("Base de datos no disponible."));
    }

    // -------------------------------------------------------------------------
    // 500 — Excepción genérica no controlada
    // -------------------------------------------------------------------------

    @Test
    void getAll_unexpectedException_returns500WithGenericMessage() throws Exception {
        when(productService.getAll()).thenThrow(new RuntimeException("NPE interno"));

        mockMvc.perform(get("/v1/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                // El mensaje interno NO debe filtrarse al cliente
                .andExpect(jsonPath("$.message").value(
                        "Ocurrió un error interno inesperado. Por favor, intentá más tarde."))
                .andExpect(jsonPath("$.path").value("/v1/products"));
    }

    // -------------------------------------------------------------------------
    // 400 — JSON malformado en el body
    // -------------------------------------------------------------------------

    @Test
    void create_malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ precio_mal: ")) // JSON inválido
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(
                        "El cuerpo del request es inválido o tiene formato incorrecto."));
    }
}
