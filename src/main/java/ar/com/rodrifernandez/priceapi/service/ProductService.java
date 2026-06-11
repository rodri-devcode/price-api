package ar.com.rodrifernandez.priceapi.service;

import ar.com.rodrifernandez.priceapi.dto.ProductRequest;
import ar.com.rodrifernandez.priceapi.dto.ProductResponse;
import ar.com.rodrifernandez.priceapi.entity.Product;
import ar.com.rodrifernandez.priceapi.exception.ProductNotFoundException;
import ar.com.rodrifernandez.priceapi.mapper.ProductMapper;
import ar.com.rodrifernandez.priceapi.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ProductResponse> getAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public ProductResponse getById(Long id) {
        return repository.findById(id)
            .map(mapper::toResponse)
            .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<ProductResponse> getByStore(String store) {
    	List<Product> results = repository.findByStoreIgnoreCase(store);
        if (results.isEmpty()) {
            throw new ProductNotFoundException("store", store);
        }
        return mapper.toResponseList(results);
    }

    public List<ProductResponse> getByType(String type) {
        List<Product> results = repository.findByTypeIgnoreCase(type);
        if (results.isEmpty()) {
            throw new ProductNotFoundException("type", type);
        }
        return mapper.toResponseList(results);
    }

    @Transactional
    public ProductResponse create(ProductRequest product) {
        return mapper.toResponse(repository.save(mapper.toEntity(product)));
    }
    
    @Transactional
    public void deleteAll() {
    	repository.deleteAll();
    }
}