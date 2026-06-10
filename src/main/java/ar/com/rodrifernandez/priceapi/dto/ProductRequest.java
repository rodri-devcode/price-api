package ar.com.rodrifernandez.priceapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Request to create a Product")
public class ProductRequest {

    @Schema(description = "Product Type", example = "Azucar")
    private String type;

    @Schema(description = "Brand", example = "Domino")
    private String brand;

    @Schema(description = "Quantity", example = "1 Kg")
    private String quantity;

    @Schema(description = "Price", example = "1100")
    private BigDecimal price;

    @Schema(description = "Store Name", example = "Dia")
    private String store;

    @Schema(description = "Product Category", example = "Almacen")
    private String category;

    public ProductRequest() {}

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}