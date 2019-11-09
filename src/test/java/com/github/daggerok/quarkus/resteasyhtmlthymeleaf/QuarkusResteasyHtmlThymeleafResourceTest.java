package com.github.daggerok.quarkus.resteasyhtmlthymeleaf;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class QuarkusResteasyHtmlThymeleafResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/")
          .then()
             .statusCode(200)
             .body(containsString("hello"));
    }
}
