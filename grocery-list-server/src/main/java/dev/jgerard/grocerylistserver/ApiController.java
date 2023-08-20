package dev.jgerard.grocerylistserver;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final GroceryItemRepository repository;

    public ApiController(GroceryItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/grocery-list")
    public List<GroceryItem> getGroceryList() {
        return repository.findAll();
    }

    @GetMapping("/grocery-options")
    public GroceryItemName[] getGroceryOptions() {
        return GroceryItemName.values();
    }

    @DeleteMapping("/grocery-list")
    public void deleteGroceryList() {
        repository.deleteAll();
    }

    @PostMapping("/grocery-item/{name}")
    public GroceryItem addGroceryItem(
            @PathVariable String name,
            @RequestParam(name = "q", defaultValue = "1") int quantity) {
        if (repository.count() >= 99)
            throw new IllegalStateException("Grocery list is full");

        GroceryItemName itemName;
        try {
            itemName = GroceryItemName.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid grocery item name: " + name);
        }
        var item = new GroceryItem(itemName, quantity);
        return repository.save(item);
    }

    @DeleteMapping("/grocery-item/{id}")
    public void deleteGroceryItem(@PathVariable Long id) {
        if (!repository.existsById(id))
            throw new NoSuchElementException("Grocery item #%d not found".formatted(id));
        repository.deleteById(id);
    }

    @PatchMapping("/grocery-item/{id}")
    public GroceryItem updateGroceryItemQuantity(
            @PathVariable Long id,
            @RequestParam(name = "q") int quantity) {
        GroceryItem item = null;
        try {
            item = repository.findById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Grocery item #%d not found".formatted(id));
        }
        item.setQuantity(quantity);
        return repository.save(item);
    }
}
