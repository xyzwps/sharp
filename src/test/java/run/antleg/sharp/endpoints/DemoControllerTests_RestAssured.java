package run.antleg.sharp.endpoints;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import run.antleg.sharp.modules.Facts;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoControllerTests_RestAssured {

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void demo() {
        given()
                .get("/demo")
                .then()
                .statusCode(200)
                .header(Facts.HEADER_X_REQUEST_ID, notNullValue())
                .body("ok", is(true));
    }
}
