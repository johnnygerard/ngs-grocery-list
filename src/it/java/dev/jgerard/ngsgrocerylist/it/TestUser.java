package dev.jgerard.ngsgrocerylist.it;

import dev.jgerard.ngsgrocerylist.entities.Product;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

public class TestUser {
    private final String username;
    private final String password;
    private final List<Product> groceryList;
    private final HttpHeaders jwtHeader = new HttpHeaders();

    public TestUser(String username, String password, List<Product> groceryList) {
        this.username = username;
        this.password = password;
        this.groceryList = groceryList;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Return a mutable copy of the grocery list.
     */
    public List<Product> getGroceryList() {
        return new ArrayList<>(groceryList);
    }

    public HttpHeaders getJwtHeader() {
        return jwtHeader;
    }

    /**
     * Return user credentials in the application/x-www-form-urlencoded format.
     */
    public String getCredentials() {
        return "username=%s&password=%s".formatted(username, password);
    }
}
