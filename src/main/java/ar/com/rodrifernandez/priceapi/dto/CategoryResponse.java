package ar.com.rodrifernandez.priceapi.dto;
 
import io.swagger.v3.oas.annotations.media.Schema;
 
@Schema(description = "Category information embedded in product responses")
public record CategoryResponse(
    @Schema(description = "Category ID", example = "2") Long id,
    @Schema(description = "Category name", example = "Almacen") String name
) {}