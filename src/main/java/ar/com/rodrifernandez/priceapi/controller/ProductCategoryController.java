package ar.com.rodrifernandez.priceapi.controller;

import ar.com.rodrifernandez.priceapi.entity.ProductCategory;
import ar.com.rodrifernandez.priceapi.service.ProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/categories")
@Tag(name = "Categories", description = "Manage product categories")
public class ProductCategoryController {

    private final ProductCategoryService service;

    public ProductCategoryController(ProductCategoryService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all categories")
    public List<ProductCategory> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id")
    public ResponseEntity<ProductCategory> getById(@Parameter(description = "Category id") @PathVariable Long id) {
        Optional<ProductCategory> category = service.getById(id);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create category")
    public ResponseEntity<ProductCategory> create(@RequestBody ProductCategory category) {
        ProductCategory saved = service.create(category);
        return ResponseEntity.created(URI.create("/v1/categories/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category")
    public ResponseEntity<ProductCategory> update(@Parameter(description = "Category id") @PathVariable Long id, @RequestBody ProductCategory category) {
        Optional<ProductCategory> updated = service.update(id, category);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category")
    public ResponseEntity<Void> delete(@Parameter(description = "Category id") @PathVariable Long id) {
        Optional<ProductCategory> existing = service.getById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
