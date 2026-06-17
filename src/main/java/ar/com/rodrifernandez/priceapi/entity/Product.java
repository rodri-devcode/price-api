package ar.com.rodrifernandez.priceapi.entity;
 
import java.math.BigDecimal;
import java.time.LocalDate;
 
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
 
@Entity
public class Product {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String brand;
    private String quantity;
    private BigDecimal price;
    private String store;
    private LocalDate priceDate;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "category_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_product_category")
    )
    private ProductCategory category;
 
    public Product() {}
 
    public Product(Long id, String type, String brand, String quantity, BigDecimal price,
                   String store, LocalDate priceDate, ProductCategory category) {
        this.id = id;
        this.type = type;
        this.brand = brand;
        this.quantity = quantity;
        this.price = price;
        this.store = store;
        this.priceDate = priceDate;
        this.category = category;
    }
 
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDate getPriceDate() {
		return priceDate;
	}

	public void setPriceDate(LocalDate priceDate) {
		this.priceDate = priceDate;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}
	
	@PrePersist
    protected void onPersist() {
        this.priceDate = LocalDate.now();
    }	
}