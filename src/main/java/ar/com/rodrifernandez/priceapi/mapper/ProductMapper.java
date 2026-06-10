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
        product.setType(request.getType());
        product.setBrand(request.getBrand());
        product.setQuantity(request.getQuantity());
        product.setPrice(request.getPrice());
        product.setStore(request.getStore());
        product.setCategory(request.getCategory());
        return product;
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setType(product.getType());
        response.setBrand(product.getBrand());
        response.setQuantity(product.getQuantity());
        response.setPrice(product.getPrice());
        response.setStore(product.getStore());
        response.setCategory(product.getCategory());
        return response;
    }
    
    public List<ProductResponse> toResponseList(List<Product> products) {
		return products.stream()
			.map(this::toResponse)
			.collect(Collectors.toList());
    }
}