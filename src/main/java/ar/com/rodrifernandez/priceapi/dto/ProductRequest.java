package ar.com.rodrifernandez.priceapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Request to create a Product")
public record ProductRequest(
    @Schema(description = "Product Type", example = "Azucar") String type,
    @Schema(description = "Brand", example = "Domino") String brand,
    @Schema(description = "Quantity", example = "1 Kg") String quantity,
    @Schema(description = "Price", example = "1100") BigDecimal price,
    @Schema(description = "Store Name", example = "Dia") String store,
    @Schema(description = "Product Category", example = "Almacen") String category
) {}