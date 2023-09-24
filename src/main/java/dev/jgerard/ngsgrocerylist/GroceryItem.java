package dev.jgerard.ngsgrocerylist;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class GroceryItem {
    @Id
    @GeneratedValue
    private Long id;
    private GroceryItemName name;
    @Min(1)
    @Max(99)
    private int quantity;

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
        this.quantity = quantity;
    }
}
