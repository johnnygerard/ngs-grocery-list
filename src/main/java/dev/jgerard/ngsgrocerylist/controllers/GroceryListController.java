package dev.jgerard.ngsgrocerylist.controllers;

import dev.jgerard.ngsgrocerylist.ProductName;
import dev.jgerard.ngsgrocerylist.entities.Product;
import dev.jgerard.ngsgrocerylist.entities.User;
import dev.jgerard.ngsgrocerylist.repositories.ProductRepository;
import dev.jgerard.ngsgrocerylist.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/products")
@Transactional
public class GroceryListController {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public GroceryListController(
        ProductRepository productRepository,
        UserRepository userRepository
    ) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private Long getUserId(Authentication authentication) {
        return Long.valueOf(authentication.getName());
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<Product> getAllProducts(Authentication authentication) {
        User user = userRepository
            .findById(getUserId(authentication))
            .orElseThrow();
        return productRepository.findAllByUser(user);
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(
        @RequestBody @Valid Product product,
        Authentication authentication
    ) {
        if (productRepository.count() > 99) throw new IllegalStateException("Grocery list is full");
        // Ensure the product ID is auto-generated
        if (product.getId() != null) throw new IllegalArgumentException("Product ID must be null");

        User user = userRepository.findById(getUserId(authentication)).orElseThrow();
        product.setUser(user);
        productRepository.save(product);
        URI location = URI.create("/api/products/" + product.getId());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllProducts(Authentication authentication) {
        User user = userRepository.findById(getUserId(authentication)).orElseThrow();
        productRepository.deleteAllByUser(user);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{productId}")
    public ResponseEntity<Void> updateQuantity(
        @PathVariable Long productId,
        @RequestParam int quantity
    ) {
        Product product;
        try {
            product = productRepository.findById(productId).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Product #%d not found".formatted(productId));
        }
        product.setQuantity(quantity);
        productRepository.save(product);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productRepository.deleteById(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("names")
    @Transactional(readOnly = true)
    public ProductName[] getAllProductNames() {
        return ProductName.values();
    }
}
