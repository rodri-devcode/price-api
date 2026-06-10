package ar.com.rodrifernandez.priceapi.service;

import ar.com.rodrifernandez.priceapi.entity.Product;
import ar.com.rodrifernandez.priceapi.entity.ProductCategory;
import ar.com.rodrifernandez.priceapi.dto.ProductRequest;
import ar.com.rodrifernandez.priceapi.dto.ProductResponse;
import ar.com.rodrifernandez.priceapi.repository.ProductCategoryRepository;
import ar.com.rodrifernandez.priceapi.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getByStore(String store) {
        return productRepository.findByStore(store);
    }

    @Transactional
    public Product create(Product product) {
        // Ensure category is managed: if category provided with id use it, otherwise try find by name or persist new
        ProductCategory cat = product.getCategory();
        if (cat != null) {
            if (cat.getId() != null) {
                Optional<ProductCategory> existing = categoryRepository.findById(cat.getId());
                existing.ifPresent(product::setCategory);
            } else if (cat.getName() != null) {
                Optional<ProductCategory> byName = categoryRepository.findByName(cat.getName());
                if (byName.isPresent()) {
                    product.setCategory(byName.get());
                } else {
                    // save new category first
                    ProductCategory saved = categoryRepository.save(cat);
                    product.setCategory(saved);
                }
            }
        }

        return productRepository.save(product);
    }

    /**
     * Create from DTO request by converting to entity and delegating to create(Product).
     */
    public Product createFromRequest(ProductRequest req) {
        Product product = new Product();
        product.setType(req.getType());
        product.setBrand(req.getBrand());
        product.setQuantity(req.getQuantity());
        product.setPrice(req.getPrice());
        product.setStore(req.getStore());
        if (req.getCategoryId() != null) {
            // attach a category stub with id so create() will resolve it
            ProductCategory cat = new ProductCategory();
            cat.setId(req.getCategoryId());
            product.setCategory(cat);
        }
        return create(product);
    }

    public ProductResponse toResponse(Product product) {
        ProductResponse resp = new ProductResponse();
        resp.setId(product.getId());
        resp.setType(product.getType());
        resp.setBrand(product.getBrand());
        resp.setQuantity(product.getQuantity());
        resp.setPrice(product.getPrice());
        resp.setStore(product.getStore());
        resp.setCategory(product.getCategory() != null ? product.getCategory().getName() : null);
        return resp;
    }
}
