package dev.jgerard.ngsgrocerylist.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.jgerard.ngsgrocerylist.ProductName;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private ProductName name;
    @Min(1)
    @Max(99)
    private int quantity;
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductName getName() {
        return name;
    }

    public void setName(ProductName name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
