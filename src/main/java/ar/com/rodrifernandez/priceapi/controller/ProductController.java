package ar.com.rodrifernandez.priceapi.controller;

import ar.com.rodrifernandez.priceapi.dto.ProductRequest;
import ar.com.rodrifernandez.priceapi.dto.ProductResponse;
import ar.com.rodrifernandez.priceapi.service.ProductService;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/products")
@Tag(name = "Products", description = "Operations related to products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	@Operation(summary = "Get all products", description = "Returns a list of products")
	public List<ProductResponse> getAll() {
		return productService.getAll();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get product by id", description = "Returns a single product by its id")
	public ProductResponse getById(@Parameter(description = "Id of the product") @PathVariable Long id) {
		return productService.getById(id);
	}

	@GetMapping("/store/{store}")
	@Operation(summary = "Get products by store", description = "Returns products filtered by store name")
	public List<ProductResponse> getByStore(@Parameter(description = "Store name") @PathVariable String store) {
		return productService.getByStore(store);
	}

	@GetMapping("/type/{type}")
	@Operation(summary = "Get products by type", description = "Returns products filtered by type")
	public List<ProductResponse> getByType(@Parameter(description = "Product type") @PathVariable String type) {
		return productService.getByType(type);
	}

	@PostMapping
	@Operation(summary = "Create product", description = "Create a new product")
	public ResponseEntity<ProductResponse> create(@RequestBody @Valid ProductRequest request) {
	    return ResponseEntity
	            .status(HttpStatus.CREATED)
	            .body(productService.create(request));
	}
	
	@DeleteMapping
	@Operation(summary = "Delete all products", description = "Deletes all products from the database. Use with caution!")
	public ResponseEntity<Void> deleteAll() {
	    productService.deleteAll();
	    return ResponseEntity.noContent().build();
	}
}