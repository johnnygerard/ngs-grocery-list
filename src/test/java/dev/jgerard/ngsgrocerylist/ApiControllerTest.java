package dev.jgerard.ngsgrocerylist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiController.class)
class ApiControllerTest {
    private static final String BASE_URL = "/api/products";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductRepository repository;

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

    private String serialize(Product product) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(product);
    }
}
