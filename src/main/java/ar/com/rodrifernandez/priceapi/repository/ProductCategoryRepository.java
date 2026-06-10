package ar.com.rodrifernandez.priceapi.repository;

import ar.com.rodrifernandez.priceapi.entity.ProductCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    Optional<ProductCategory> findByName(String name);
}
