package dev.jgerard.ngsgrocerylist.it;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jgerard.ngsgrocerylist.ProductName;
import dev.jgerard.ngsgrocerylist.entities.Product;
import org.json.JSONException;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class ApiIT {
    private static final String BASE_URL = "/api/products";
    // Most tests will target the main user.
    private static final String MAIN_USER = "Alice";
    // The main reason for having a secondary user is to
    // ensure grocery lists are user-specific.
    private static final String SECONDARY_USER = "Bob";
    private static final String CREDENTIALS_1 = MAIN_USER + ",pdH8W8zL";
    private static final String CREDENTIALS_2 = SECONDARY_USER + ",P5uQ9iHH";
    private static final String CREDENTIALS_TEMPLATE = "username=%s&password=%s";
    private static final List<String> jwtList = new ArrayList<>();
    private static final List<Product> mainGroceryList;
    private static final List<Product> secondaryGroceryList;

    static {
        mainGroceryList = List.of(
            makeProduct(ProductName.APPLES, 3),
            makeProduct(ProductName.BANANAS, 2),
            makeProduct(ProductName.CARROTS, 1)
        );

        secondaryGroceryList = List.of(
            makeProduct(ProductName.BREAD, 1),
            makeProduct(ProductName.MILK, 10)
        );
    }

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private static Product makeProduct(ProductName name, int quantity) {
        var product = new Product();
        product.setName(name);
        product.setQuantity(quantity);
        return product;
    }

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SetupTests {
        @ParameterizedTest
        @Order(1)
        @CsvSource({
            CREDENTIALS_1,
            CREDENTIALS_2
        })
        void registerUser(String username, String password) {
            var request = RequestEntity.post("/api/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(CREDENTIALS_TEMPLATE.formatted(username, password));
            var response = restTemplate.exchange(request, String.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(MediaType.TEXT_PLAIN.equalsTypeAndSubtype(
                response.getHeaders().getContentType())
            );

            jwtList.add(response.getBody());
        }

        @Test
        @Order(2)
        void addProducts() throws JsonProcessingException, JSONException {
            var locationPattern = Pattern.compile(BASE_URL + "/(\\d+)");
            var groceryLists = List.of(mainGroceryList, secondaryGroceryList);

            for (int i = 0; i < groceryLists.size(); i++) {
                List<Product> groceryList = groceryLists.get(i);
                String jwt = jwtList.get(i);

                for (Product product : groceryList) {
                    var request = RequestEntity.post(BASE_URL)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(product);
                    var response = restTemplate.exchange(request, Void.class);
                    URI location = response.getHeaders().getLocation();

                    assertEquals(HttpStatus.CREATED, response.getStatusCode());
                    assertNotNull(location, "Location header not found");
                    var matcher = locationPattern.matcher(location.toString());
                    assertTrue(matcher.matches());
                    product.setId(Long.valueOf(matcher.group(1)));
                    assertNull(response.getBody());
                }

                var request = RequestEntity.get(BASE_URL)
                    .header("Authorization", "Bearer " + jwt)
                    .build();
                var response = restTemplate.exchange(request, String.class);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
                JSONAssert.assertEquals(
                    objectMapper.writeValueAsString(groceryList),
                    response.getBody(),
                    JSONCompareMode.NON_EXTENSIBLE
                );
            }
        }
    }

    @Nested
    @Order(2)
    class CoreTests {
        @Test
        void deleteAllProducts() throws JSONException {
            var request = RequestEntity.delete(BASE_URL)
                .header("Authorization", "Bearer " + getJwt())
                .build();
            var response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            assertNull(response.getBody());
            JSONAssert.assertEquals("[]", getGroceryList(), true);
        }

        private String getJwt() {
            return jwtList.get(0);
        }

        private String getGroceryList() {
            var request = RequestEntity.get(BASE_URL)
                .header("Authorization", "Bearer " + getJwt())
                .build();
            return restTemplate.exchange(request, String.class).getBody();
        }
    }
}
