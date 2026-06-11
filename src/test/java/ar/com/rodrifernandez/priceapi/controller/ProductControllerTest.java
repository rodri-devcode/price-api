package ar.com.rodrifernandez.priceapi.controller;

import ar.com.rodrifernandez.priceapi.dto.ProductRequest;
import ar.com.rodrifernandez.priceapi.dto.ProductResponse;
import ar.com.rodrifernandez.priceapi.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import ar.com.rodrifernandez.priceapi.exception.GlobalExceptionHandler;
import ar.com.rodrifernandez.priceapi.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ProductResponse sampleResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ProductController controller = new ProductController(productService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        sampleResponse = new ProductResponse(1L, "Azucar", "Domino", "1 Kg", new BigDecimal("1100"), "Dia", LocalDate.now(), "Almacen");
    }

    @Test
    void getAll_returnsList() throws Exception {
        when(productService.getAll()).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/v1/products").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getById_found_returnsBody() throws Exception {
        when(productService.getById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/v1/products/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        when(productService.getById(2L)).thenThrow(new ProductNotFoundException(2L));

        mockMvc.perform(get("/v1/products/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                    .value("El producto con id 2 no fue encontrado."));
    }

    @Test
    void getByStore_returnsList() throws Exception {
        when(productService.getByStore("Dia")).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/v1/products/store/Dia").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].store").value("Dia"));
    }

    @Test
    void getByType_returnsList() throws Exception {
        when(productService.getByType("Azucar")).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/v1/products/type/Azucar").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].type").value("Azucar"));
    }

    @Test
    void create_returnsCreatedEntity() throws Exception {
        ProductRequest req = new ProductRequest("Azucar", "Domino", "1 Kg", new BigDecimal("1100"), "Dia", "Almacen");
        when(productService.create(any(ProductRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteAll_invokesService() throws Exception {
        doNothing().when(productService).deleteAll();

        mockMvc.perform(delete("/v1/products")).andExpect(status().isNoContent());
    }

    @Test
    void getAll_serviceThrows_internalServerError() throws Exception {
        when(productService.getAll()).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/v1/products").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(500));
//            .andExpect(jsonPath("$.message").value("boom"));
    }

}
