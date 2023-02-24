package run.antleg.sharp.endpoints;

import io.restassured.RestAssured;
import lombok.Data;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import run.antleg.sharp.test.tc.Mysql;
import run.antleg.sharp.test.util.PasswordUtils;
import run.antleg.sharp.test.util.UsernameUtils;
import run.antleg.sharp.util.DateUtils;
import run.antleg.sharp.util.JSONObject;

import java.time.LocalDateTime;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static run.antleg.sharp.util.CollectionUtils.*;

@ActiveProfiles("tc")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ControllerTestsBase {

    private static final int port = 41592;

    private static final String baseUrl = "http://localhost";

    static final String serverPrefix = baseUrl + ":" + port;

    @Autowired
    TestRestTemplate restTemplate;

    @ClassRule
    public static final Mysql mysql = Mysql.getInstance();

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.port = port;
    }


    /**
     * 其正确性由 {@link RegisterControllerTests} 保证。
     */
    void naiveRegister(BiConsumer<String, String> bc) {
        var username = UsernameUtils.randomUsername();
        var password = PasswordUtils.randomPassword();
        var response = postRegisterNaive(username, password);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        var respBody = response.getBody();
        assertNotNull(respBody);
        assertThat(respBody.getBoolean("ok")).isNotNull().isTrue();
        bc.accept(username, password);
    }

    protected ResponseEntity<JSONObject> postRegisterNaive(String username, String password) {
        var api = serverPrefix + "/api/register/naive";
        var body = mutMap("username", username, "password", password);
        return this.restTemplate.postForEntity(api, body, JSONObject.class);
    }

    protected ResponseEntity<JSONObject> postRestLogin(String username, String password) {
        var api = serverPrefix + "/api/login";
        var body = mutMap("username", username, "password", password);
        return this.restTemplate.postForEntity(api, body, JSONObject.class);
    }

    @Data
    public static class NaiveRegisterAndRestLoginResult {
        private Integer userId;
        private String username;
        private String displayName;
        private String cookie;
    }


    /**
     * 其正确性由 {@link #naiveRegister} 和 {@link RestLoginControllerTests} 保证。
     */
    NaiveRegisterAndRestLoginResult naiveRegisterAndRestLogin() {
        var result = new NaiveRegisterAndRestLoginResult();
        naiveRegister((username, password) -> {
            var resp = postRestLogin(username, password);
            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
            var respBody = resp.getBody();
            assertNotNull(respBody);

            assertThat(respBody.get("id"))
                    .isNotNull()
                    .isInstanceOf(Number.class);
            result.setUserId(respBody.getInteger("id"));

            assertThat(respBody.getString("username"))
                    .isNotNull()
                    .isEqualTo(username);
            result.setUsername(respBody.getString("username"));

            assertThat(respBody.getString("displayName"))
                    .isNotNull()
                    .isEqualTo(username);
            result.setDisplayName(respBody.getString("displayName"));

            assertThat(respBody.getString("registerTime"))
                    .isNotNull()
                    .matches(DateUtils.dateTimeFormatterPatternRegExp)
                    .isLessThanOrEqualTo(LocalDateTime.now().format(DateUtils.dateTimeFormatter));

            result.setCookie(resp.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        });
        return result;
    }
}
