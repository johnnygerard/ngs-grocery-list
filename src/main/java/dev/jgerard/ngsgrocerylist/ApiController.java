package dev.jgerard.ngsgrocerylist;

import org.springframework.web.bind.annotation.*;

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

    @GetMapping("names")
    public ProductName[] getGroceryOptions() {
        return ProductName.values();
    }

    @DeleteMapping
    public void deleteProducts() {
        repository.deleteAll();
    }

    @PostMapping("{name}")
    public Long addProduct(
            @PathVariable String name,
            @RequestParam(name = "q", defaultValue = "1") int quantity) {
        if (repository.count() >= 99) throw new IllegalStateException("Grocery list is full");

        ProductName productName;
        try {
            productName = ProductName.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid product name: " + name);
        }
        var product = new Product();
        product.setName(productName);
        product.setQuantity(quantity);
        repository.save(product);
        return product.getId();
    }

    @DeleteMapping("{id}")
    public void deleteProduct(@PathVariable Long id) {
        if (!repository.existsById(id))
            throw new NoSuchElementException("Product #%d not found".formatted(id));
        repository.deleteById(id);
    }

    @PatchMapping("{id}")
    public void updateProductQuantity(
            @PathVariable Long id,
            @RequestParam(name = "q") int quantity) {
        Product product = null;
        try {
            product = repository.findById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Product #%d not found".formatted(id));
        }
        product.setQuantity(quantity);
        repository.save(product);
    }
}
