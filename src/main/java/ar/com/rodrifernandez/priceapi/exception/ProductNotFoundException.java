package ar.com.rodrifernandez.priceapi.exception;

/**
 * Se lanza cuando se busca un producto por ID o criterio y no existe en la base de datos.
 * Hereda de RuntimeException (unchecked) para no contaminar las firmas de los métodos
 * de servicio con throws obligatorios — el handler global la intercepta automáticamente.
 */
public class ProductNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
	private final Long productId;

    /**
     * Constructor para búsquedas fallidas por ID numérico.
     * El mensaje ya viene formateado y listo para devolver al cliente.
     */
    public ProductNotFoundException(Long productId) {
        super("El producto con id " + productId + " no fue encontrado.");
        this.productId = productId;
    }

    /**
     * Constructor para búsquedas fallidas por criterio de texto (store, type, etc.).
     */
    public ProductNotFoundException(String field, String value) {
        super("No se encontraron productos con " + field + " = '" + value + "'.");
        this.productId = null;
    }

    public Long getProductId() {
        return productId;
    }
}
