package ar.com.rodrifernandez.priceapi.repository;

import ar.com.rodrifernandez.priceapi.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStore(String store);
}