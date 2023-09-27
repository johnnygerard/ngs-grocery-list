package dev.jgerard.ngsgrocerylist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiController.class)
class ApiControllerTest {
    private static final String BASE_URL = "/api/products";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductRepository repository;

    // Helper method to create a product
    private Product createProduct(Long id, ProductName name, int quantity) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setQuantity(quantity);
        return product;
    }

    // Helper method to serialize an object to JSON
    private String serialize(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    @Test
    void getAllProducts() throws Exception {
        // given
        List<Product> productList = List.of(
            createProduct(1L, ProductName.APPLES, 10),
            createProduct(2L, ProductName.BANANAS, 20),
            createProduct(3L, ProductName.CARROTS, 30)
        );
        given(repository.findAll()).willReturn(productList);

        // when
        mockMvc.perform(get(BASE_URL))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(serialize(productList)));

        verify(repository).findAll();
    }

    @Test
    void addProduct() throws Exception {
        // given
        Long productId = 2L;
        var product = new Product();
        product.setName(ProductName.APPLES);
        product.setQuantity(10);
        given(repository.save(any(Product.class))).willAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(productId); // Simulate auto-generated ID
            return p;
        });

        // when
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(product)))
            // then
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "%s/%d".formatted(BASE_URL, productId)));

        verify(repository).save(any(Product.class));
    }

    @Test
    void deleteAllProducts() throws Exception {
        // when
        mockMvc.perform(delete(BASE_URL))
            // then
            .andExpect(status().isNoContent());

        verify(repository).deleteAll();
    }

    @Test
    void updateQuantity() throws Exception {
        // given
        Long productId = 3L;
        int quantity = 10;
        Product product = createProduct(productId, ProductName.APPLES, 20);
        given(repository.findById(productId))
            .willReturn(Optional.of(product));

        // when
        mockMvc.perform(patch(BASE_URL + "/{productId}", productId)
                .param("quantity", String.valueOf(quantity)))
            // then
            .andExpect(status().isNoContent());

        verify(repository).save(any(Product.class));
        assertEquals("Quantity update failed", quantity, product.getQuantity());
    }

    @Test
    void deleteProduct() throws Exception {
        // given
        Long productId = 4L;

        // when
        mockMvc.perform(delete(BASE_URL + "/{productId}", productId))
            // then
            .andExpect(status().isNoContent());

        verify(repository).deleteById(productId);
    }

    @Test
    void getAllProductNames() throws Exception {
        // when
        mockMvc.perform(get(BASE_URL + "/names"))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(serialize(ProductName.values())));
    }
}
