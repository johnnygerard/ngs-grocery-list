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
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;

import java.net.URI;
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
    private static final List<TestUser> testUsers = List.of(
        new TestUser("Alice", "pdH8W8zL", List.of(
            makeProduct(ProductName.APPLES, 3),
            makeProduct(ProductName.BANANAS, 2),
            makeProduct(ProductName.CARROTS, 1)
        )),
        new TestUser("Bob", "P5uQ9iHH", List.of(
            makeProduct(ProductName.BREAD, 1),
            makeProduct(ProductName.MILK, 10)
        )),
        new TestUser("Carol", "8zLpdH8W", List.of(
            makeProduct(ProductName.CARROTS, 2),
            makeProduct(ProductName.EGGS, 12)
        ))
    );

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
        @Test
        @Order(1)
        void registerUser() {
            for (TestUser testUser : testUsers) {
                var request = RequestEntity.post("/api/register")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(testUser.getCredentials());
                var response = restTemplate.exchange(request, String.class);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertTrue(MediaType.TEXT_PLAIN.equalsTypeAndSubtype(
                    response.getHeaders().getContentType())
                );

                String jwt = response.getBody();
                assertNotNull(jwt);
                testUser.getJwtHeader().set("Authorization", "Bearer " + jwt);
            }
        }

        @Test
        @Order(2)
        void addProducts() throws JsonProcessingException, JSONException {
            var locationPattern = Pattern.compile(BASE_URL + "/(\\d+)");

            for (TestUser testUser : testUsers) {
                List<Product> groceryList = testUser.getGroceryList();

                for (Product product : groceryList) {
                    var request = RequestEntity.post(BASE_URL)
                        .headers(testUser.getJwtHeader())
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
                    .headers(testUser.getJwtHeader())
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
            var testUser = getRandomTestUser();
            var request = RequestEntity.delete(BASE_URL)
                .headers(testUser.getJwtHeader())
                .build();
            var response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            assertNull(response.getBody());
            JSONAssert.assertEquals("[]", getActualGroceryList(testUser), true);
        }

        private TestUser getRandomTestUser() {
            var index = (int) (Math.random() * testUsers.size());
            return testUsers.get(index);
        }

        private String getActualGroceryList(TestUser testUser) {
            var request = RequestEntity.get(BASE_URL)
                .headers(testUser.getJwtHeader())
                .build();
            return restTemplate.exchange(request, String.class).getBody();
        }
    }
}
