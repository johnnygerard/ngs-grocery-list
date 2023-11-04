package dev.jgerard.ngsgrocerylist.repositories;

import dev.jgerard.ngsgrocerylist.entities.Product;
import dev.jgerard.ngsgrocerylist.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByUser(User user);
    void deleteAllByUser(User user);
}
