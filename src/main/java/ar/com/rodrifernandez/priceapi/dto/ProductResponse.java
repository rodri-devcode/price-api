package ar.com.rodrifernandez.priceapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Product response returned by API")
public record ProductResponse(
    @Schema(description = "Product ID", example = "1") Long id,
    @Schema(description = "Product Type", example = "Azucar") String type,
    @Schema(description = "Brand", example = "Domino") String brand,
    @Schema(description = "Quantity", example = "1 Kg") String quantity,
    @Schema(description = "Price", example = "1100") BigDecimal price,
    @Schema(description = "Store Name", example = "Dia") String store,
    @Schema(description = "Price Date", example = "2023-01-01") LocalDate priceDate,
    @Schema(description = "Category Name", example = "Almacen") String category
) {}