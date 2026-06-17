package ar.com.rodrifernandez.priceapi.mapper;
 
import ar.com.rodrifernandez.priceapi.dto.CategoryResponse;
import ar.com.rodrifernandez.priceapi.dto.ProductRequest;
import ar.com.rodrifernandez.priceapi.dto.ProductResponse;
import ar.com.rodrifernandez.priceapi.entity.Product;
 
import java.util.List;
import java.util.stream.Collectors;
 
import org.springframework.stereotype.Component;
 
@Component
public class ProductMapper {
 
    /**
     * Convierte ProductRequest → Product (sin categoría).
     * La categoría se resuelve en el servicio y se setea por separado,
     * ya que requiere una consulta al repositorio.
     */
    public Product toEntity(ProductRequest request) {
        if (request == null) return null;
 
        Product product = new Product();
        product.setType(request.type());
        product.setBrand(request.brand());
        product.setQuantity(request.quantity());
        product.setPrice(request.price());
        product.setStore(request.store());
        // category se setea en ProductService tras resolver la entidad
        return product;
    }
 
    public ProductResponse toResponse(Product product) {
        if (product == null) return null;
 
        CategoryResponse categoryResponse = new CategoryResponse(
            product.getCategory().getId(),
            product.getCategory().getName()
        );
 
        return new ProductResponse(
            product.getId(),
            product.getType(),
            product.getBrand(),
            product.getQuantity(),
            product.getPrice(),
            product.getStore(),
            product.getPriceDate(),
            categoryResponse
        );
    }
 
    public List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
}