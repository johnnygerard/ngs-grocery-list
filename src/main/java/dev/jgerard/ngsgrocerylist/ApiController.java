package dev.jgerard.ngsgrocerylist;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/grocery-items")
public class ApiController {
    private final GroceryItemRepository repository;

    public ApiController(GroceryItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<GroceryItem> getGroceryItems() {
        return repository.findAll();
    }

    @GetMapping("names")
    public GroceryItemName[] getGroceryOptions() {
        return GroceryItemName.values();
    }

    @DeleteMapping
    public void deleteGroceryItems() {
        repository.deleteAll();
    }

    @PostMapping("{name}")
    public Long addGroceryItem(
            @PathVariable String name,
            @RequestParam(name = "q", defaultValue = "1") int quantity) {
        if (repository.count() >= 99) throw new IllegalStateException("Grocery list is full");

        GroceryItemName itemName;
        try {
            itemName = GroceryItemName.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid grocery item name: " + name);
        }
        var item = new GroceryItem(itemName, quantity);
        repository.save(item);
        return item.getId();
    }

    @DeleteMapping("{id}")
    public void deleteGroceryItem(@PathVariable Long id) {
        if (!repository.existsById(id))
            throw new NoSuchElementException("Grocery item #%d not found".formatted(id));
        repository.deleteById(id);
    }

    @PatchMapping("{id}")
    public void updateGroceryItemQuantity(
            @PathVariable Long id,
            @RequestParam(name = "q") int quantity) {
        GroceryItem item = null;
        try {
            item = repository.findById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Grocery item #%d not found".formatted(id));
        }
        item.setQuantity(quantity);
        repository.save(item);
    }
}
