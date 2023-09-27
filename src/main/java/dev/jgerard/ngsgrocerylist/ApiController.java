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
    public List<Product> getProducts() {
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
    public void deleteProducts() {
        repository.deleteAll();
    }

    @PatchMapping("{id}")
    public void updateProductQuantity(
        @PathVariable Long id,
        @RequestParam(name = "q") int quantity
    ) {
        Product product = null;
        try {
            product = repository.findById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Product #%d not found".formatted(id));
        }
        product.setQuantity(quantity);
        repository.save(product);
    }

    @DeleteMapping("{id}")
    public void deleteProduct(@PathVariable Long id) {
        if (!repository.existsById(id))
            throw new NoSuchElementException("Product #%d not found".formatted(id));
        repository.deleteById(id);
    }

    @GetMapping("names")
    public ProductName[] getGroceryOptions() {
        return ProductName.values();
    }
}
