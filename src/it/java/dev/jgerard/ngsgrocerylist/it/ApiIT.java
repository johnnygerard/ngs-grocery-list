package dev.jgerard.ngsgrocerylist.it;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class ApiIT {
    private static final String CREDENTIALS_1 = "Alice,pdH8W8zL";
    private static final String CREDENTIALS_2 = "Bob,P5uQ9iHH";
    private static final String CREDENTIALS_TEMPLATE = "username=%s&password=%s";
    private final List<String> jwts = new ArrayList<>();
    @Autowired
    private TestRestTemplate restTemplate;

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

            jwts.add(response.getBody());
        }
    }

    @Nested
    @Order(2)
    class CoreTests {

    }
}
