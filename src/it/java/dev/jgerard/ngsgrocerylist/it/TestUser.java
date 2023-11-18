package dev.jgerard.ngsgrocerylist.it;

import dev.jgerard.ngsgrocerylist.entities.Product;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

/**
 * Immutable class to hold test user information.
 */
public class TestUser {
    private final String username;
    private final String password;
    private final List<Product> groceryList;
    private HttpHeaders jwtHeader;

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

    public List<Product> getGroceryList() {
        return new ArrayList<>(groceryList);
    }

    public HttpHeaders getJwtHeader() {
        return new HttpHeaders(jwtHeader);
    }

    public void setJwtHeader(HttpHeaders jwtHeader) {
        if (this.jwtHeader == null)
            this.jwtHeader = jwtHeader;
    }
}
