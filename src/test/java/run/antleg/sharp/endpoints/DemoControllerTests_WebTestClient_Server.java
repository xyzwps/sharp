package run.antleg.sharp.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import run.antleg.sharp.modules.Facts;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoControllerTests_WebTestClient_Server {

    WebTestClient client;

    @LocalServerPort
    int port;

    @BeforeEach
    void beforeAll() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void demo() {
        client.get().uri("/demo")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectHeader().exists(Facts.HEADER_X_REQUEST_ID)

                .expectBody()
                .jsonPath("$.ok", true);
    }
}
