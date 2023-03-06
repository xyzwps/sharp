package run.antleg.sharp.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import run.antleg.sharp.modules.DemoController;
import run.antleg.sharp.modules.Facts;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class DemoControllerTests_WebTestClient_Mock {

    WebTestClient client = MockMvcWebTestClient.bindToController(new DemoController()).build();

    @Test
    void demo() {
        client.get().uri("/demo")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectHeader().doesNotExist(Facts.HEADER_X_REQUEST_ID)

                .expectBody()
                .jsonPath("$.ok", true);
    }
}
