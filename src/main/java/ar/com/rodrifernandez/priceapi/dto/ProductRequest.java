package ar.com.rodrifernandez.priceapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Request to create a Product")
public class ProductRequest {

    @Schema(description = "Product type", example = "Beverage")
    private String type;

    @Schema(description = "Brand", example = "Acme")
    private String brand;

    @Schema(description = "Quantity (as string)", example = "1L")
    private String quantity;

    @Schema(description = "Price", example = "3.5")
    private BigDecimal price;

    @Schema(description = "Store name", example = "StoreA")
    private String store;

    @Schema(description = "Category id to link the product to", example = "1")
    private Long categoryId;

    public ProductRequest() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
