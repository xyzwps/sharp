package run.antleg.sharp.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import run.antleg.sharp.modules.Facts;
import run.antleg.sharp.util.JSONObject;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoControllerTests_TestRestTemplate {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int port;

    @Test
    void demo() {
        var api = "http://localhost:" + port + "/demo";
        var resp = this.restTemplate.getForEntity(api, JSONObject.class);
        assertEquals(resp.getStatusCode(), HttpStatus.OK);
        assertTrue(resp.getHeaders().containsKey(Facts.HEADER_X_REQUEST_ID));

        var body = resp.getBody();
        assertNotNull(body);
        assertTrue(body.getBoolean("ok"));
    }
}
