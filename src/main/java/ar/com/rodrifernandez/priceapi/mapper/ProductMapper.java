package ar.com.rodrifernandez.priceapi.mapper;

import ar.com.rodrifernandez.priceapi.dto.ProductRequest;
import ar.com.rodrifernandez.priceapi.dto.ProductResponse;
import ar.com.rodrifernandez.priceapi.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        if (request == null) {
            return null;
        }
        Product product = new Product();
        product.setType(request.type());
        product.setBrand(request.brand());
        product.setQuantity(request.quantity());
        product.setPrice(request.price());
        product.setStore(request.store());
        product.setCategory(request.category());
        return product;
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductResponse(
            product.getId(),
            product.getType(),
            product.getBrand(),
            product.getQuantity(),
            product.getPrice(),
            product.getStore(),
            product.getPriceDate(),
            product.getCategory()
        );
    }
    
    public List<ProductResponse> toResponseList(List<Product> products) {
		return products.stream()
			.map(this::toResponse)
			.collect(Collectors.toList());
    }
}