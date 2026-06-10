package ar.com.rodrifernandez.priceapi.controller;

import ar.com.rodrifernandez.priceapi.dto.ProductRequest;
import ar.com.rodrifernandez.priceapi.dto.ProductResponse;
import ar.com.rodrifernandez.priceapi.entity.Product;
import ar.com.rodrifernandez.priceapi.service.ProductService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
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
		return productService.getAll().stream().map(productService::toResponse).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get product by id", description = "Returns a single product by its id")
	public ResponseEntity<ProductResponse> getById(@Parameter(description = "Id of the product") @PathVariable Long id) {
		Optional<Product> product = productService.getById(id);
		return product.map(p -> ResponseEntity.ok(productService.toResponse(p))).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/store/{store}")
	@Operation(summary = "Get products by store", description = "Returns products filtered by store name")
	public List<ProductResponse> getByStore(@Parameter(description = "Store name") @PathVariable String store) {
		return productService.getByStore(store).stream().map(productService::toResponse).collect(Collectors.toList());
	}

	@PostMapping
	@Operation(summary = "Create product", description = "Create a new product")
	public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request) {
		Product saved = productService.createFromRequest(request);
		ProductResponse resp = productService.toResponse(saved);
		return ResponseEntity.created(URI.create("/v1/products/" + saved.getId())).body(resp);
	}
}
