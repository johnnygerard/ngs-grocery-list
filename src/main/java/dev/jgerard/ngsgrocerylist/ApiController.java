package dev.jgerard.ngsgrocerylist;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/products")
public class ApiController {
    private final ProductRepository repository;

    public ApiController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(@RequestBody @Valid Product product) {
        if (repository.count() > 99) throw new IllegalStateException("Grocery list is full");
        // Ensure the product ID is auto-generated
        if (product.getId() != null) throw new IllegalArgumentException("Product ID must be null");

        repository.save(product);
        URI location = URI.create("/api/products/" + product.getId());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllProducts() {
        repository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{productId}")
    public ResponseEntity<Void> updateQuantity(
        @PathVariable Long productId,
        @RequestParam int quantity
    ) {
        Product product;
        try {
            product = repository.findById(productId).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Product #%d not found".formatted(productId));
        }
        product.setQuantity(quantity);
        repository.save(product);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        repository.deleteById(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("names")
    public ProductName[] getAllProductNames() {
        return ProductName.values();
    }
}
