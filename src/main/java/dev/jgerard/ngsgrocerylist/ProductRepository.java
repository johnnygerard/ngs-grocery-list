package dev.jgerard.ngsgrocerylist;

import dev.jgerard.ngsgrocerylist.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
