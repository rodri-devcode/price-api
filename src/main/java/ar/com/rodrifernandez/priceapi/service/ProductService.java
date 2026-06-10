package ar.com.rodrifernandez.priceapi.service;

import ar.com.rodrifernandez.priceapi.dto.ProductRequest;
import ar.com.rodrifernandez.priceapi.dto.ProductResponse;
import ar.com.rodrifernandez.priceapi.mapper.ProductMapper;
import ar.com.rodrifernandez.priceapi.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
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

    public Optional<ProductResponse> getById(Long id) {
        return repository.findById(id).map(mapper::toResponse);
    }

    public List<ProductResponse> getByStore(String store) {
        return mapper.toResponseList(repository.findByStore(store));
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