package ar.com.rodrifernandez.priceapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a Product")
public record ProductRequest(
    @Schema(description = "Product Type", example = "Azucar")
    @NotBlank(message = "type is mandatory")
    @Size(max = 100, message = "type must not exceed 100 characters")
    String type,

    @Schema(description = "Brand", example = "Domino")
    @Size(max = 100, message = "brand must not exceed 100 characters")
    String brand,

    @Schema(description = "Quantity", example = "1 Kg")
    @NotBlank(message = "quantity is mandatory")
    @Size(max = 50, message = "quantity must not exceed 50 characters")
    String quantity,

    @Schema(description = "Price", example = "1100")
    @NotNull(message = "price is mandatory")
    @PositiveOrZero(message = "price must be zero or positive")
    BigDecimal price,

    @Schema(description = "Store Name", example = "Dia")
    @NotBlank(message = "store is mandatory")
    @Size(max = 100, message = "store must not exceed 100 characters")
    String store,

    @Schema(description = "Product Category", example = "Almacen")
    @NotBlank(message = "category is mandatory")
    @Size(max = 50, message = "category must not exceed 50 characters")
    String category
) {}