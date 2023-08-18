package dev.jgerard.grocerylistserver;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class GroceryItem {
    @Id
    @GeneratedValue
    private Long id;
    private GroceryItemName name;
    private int quantity;
    private static final int MAX_QUANTITY = 99;
    private static final int MIN_QUANTITY = 1;

    public GroceryItem() {
    }

    public GroceryItem(GroceryItemName name) {
        this.name = name;
        this.quantity = MIN_QUANTITY;
    }
    public GroceryItem(GroceryItemName name, int quantity) {
        this.name = name;
        this.setQuantity(quantity);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public GroceryItemName getName() {
        return name;
    }

    public void setName(GroceryItemName name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY)
            throw new IllegalArgumentException("Quantity must be between %d and %d"
                    .formatted(MIN_QUANTITY, MAX_QUANTITY));

        this.quantity = quantity;
    }
}
