package ar.com.rodrifernandez.priceapi.service;

import ar.com.rodrifernandez.priceapi.entity.ProductCategory;
import ar.com.rodrifernandez.priceapi.repository.ProductCategoryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository repository;

    public ProductCategoryService(ProductCategoryRepository repository) {
        this.repository = repository;
    }

    public List<ProductCategory> getAll() {
        return repository.findAll();
    }

    public Optional<ProductCategory> getById(Long id) {
        return repository.findById(id);
    }

    public ProductCategory create(ProductCategory category) {
        return repository.save(category);
    }

    @Transactional
    public Optional<ProductCategory> update(Long id, ProductCategory category) {
        return repository.findById(id).map(existing -> {
            existing.setName(category.getName());
            return repository.save(existing);
        });
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
