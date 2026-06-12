package ar.com.rodrifernandez.priceapi.service;

import ar.com.rodrifernandez.priceapi.dto.ProductRequest;
import ar.com.rodrifernandez.priceapi.dto.ProductResponse;
import ar.com.rodrifernandez.priceapi.entity.Product;
import ar.com.rodrifernandez.priceapi.mapper.ProductMapper;
import ar.com.rodrifernandez.priceapi.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import ar.com.rodrifernandez.priceapi.exception.ProductNotFoundException;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductService service;

    private Product sampleEntity;
    private ProductResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleEntity = new Product(1L, "Azucar", "Domino", "1 Kg", new BigDecimal("1100"), "Dia", LocalDate.now(), "Almacen");
        sampleResponse = new ProductResponse(1L, "Azucar", "Domino", "1 Kg", new BigDecimal("1100"), "Dia", LocalDate.now(), "Almacen");
    }

    @Test
    void getAll_returnsMappedList() {
        when(repository.findAll()).thenReturn(List.of(sampleEntity));
        when(mapper.toResponseList(List.of(sampleEntity))).thenReturn(List.of(sampleResponse));

        List<ProductResponse> result = service.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleResponse, result.get(0));
        verify(repository).findAll();
        verify(mapper).toResponseList(anyList());
    }

    @Test
    void getById_found() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleEntity));
        when(mapper.toResponse(sampleEntity)).thenReturn(sampleResponse);
        ProductResponse result = service.getById(1L);

        assertNotNull(result);
        assertEquals(sampleResponse, result);
        verify(repository).findById(1L);
    }

    @Test
    void getById_notFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> service.getById(2L));
        verify(repository).findById(2L);
    }

    @Test
    void getByStore_notFound() {
        when(repository.findByStoreIgnoreCase("unknown")).thenReturn(List.of());

        // Service throws ProductNotFoundException when no results are found for a store
        assertThrows(ProductNotFoundException.class, () -> service.getByStore("unknown"));
    }

    @Test
    void getByType_returnsList() {
        when(repository.findByTypeIgnoreCase("Azucar")).thenReturn(List.of(sampleEntity));
        when(mapper.toResponseList(List.of(sampleEntity))).thenReturn(List.of(sampleResponse));

        List<ProductResponse> result = service.getByType("Azucar");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleResponse, result.get(0));
    }
    
    @Test
    void getByType_notFound() {
        when(repository.findByTypeIgnoreCase("unknown")).thenReturn(List.of());
        assertThrows(ProductNotFoundException.class, () -> service.getByType("unknown"));
    }

    @Test
    void create_success() {
        ProductRequest req = new ProductRequest("Azucar", "Domino", "1 Kg", new BigDecimal("1100"), "Dia", "Almacen");
        when(mapper.toEntity(req)).thenReturn(sampleEntity);
        when(repository.save(sampleEntity)).thenReturn(sampleEntity);
        when(mapper.toResponse(sampleEntity)).thenReturn(sampleResponse);

        ProductResponse result = service.create(req);

        assertNotNull(result);
        assertEquals(sampleResponse, result);
        verify(repository).save(sampleEntity);
    }

    @Test
    void create_repositoryThrows_exceptionPropagated() {
        ProductRequest req = new ProductRequest("Azucar", "Domino", "1 Kg", new BigDecimal("1100"), "Dia", "Almacen");
        when(mapper.toEntity(req)).thenReturn(sampleEntity);
        when(repository.save(sampleEntity)).thenThrow(new RuntimeException("db down"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.create(req));
        assertEquals("db down", ex.getMessage());
    }

    @Test
    void deleteAll_callsRepository() {
        doNothing().when(repository).deleteAll();

        service.deleteAll();

        verify(repository).deleteAll();
    }
}
