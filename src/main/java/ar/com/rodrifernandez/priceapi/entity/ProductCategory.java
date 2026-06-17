package ar.com.rodrifernandez.priceapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
 
@Entity
@Table(
    name = "product_category",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_category_name",
        columnNames = "name"
    )
)
public class ProductCategory {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false, length = 100)
    private String name;
 
    @Column(nullable = false)
    private boolean active = true;
 
    protected ProductCategory() {} // solo para JPA
 
    public ProductCategory(String name) {
        this.name = name;
    }
 
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isActive() {
		return active;
	}

	public void deactivate() {
		this.active = false;
	}
}