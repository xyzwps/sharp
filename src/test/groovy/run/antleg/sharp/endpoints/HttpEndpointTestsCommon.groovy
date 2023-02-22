package run.antleg.sharp.endpoints

import org.junit.ClassRule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import run.antleg.sharp.test.tc.Mysql
import run.antleg.sharp.test.util.PasswordUtils
import run.antleg.sharp.test.util.UsernameUtils
import run.antleg.sharp.util.DateUtils
import run.antleg.sharp.util.JSONObject

import java.time.LocalDateTime
import java.util.function.BiConsumer

import static org.assertj.core.api.Assertions.*
import static org.junit.jupiter.api.Assertions.*

@ActiveProfiles("tc")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class HttpEndpointTestsCommon {

    private static final int port = 41592

    @Autowired
    TestRestTemplate restTemplate

    static String getServerPrefix() {
        return "http://localhost:${port}"
    }

    @ClassRule
    static final Mysql mysql = Mysql.instance


    /**
     * 其正确性由 {@link RegisterControllerTests} 保证。
     */
    void naiveRegister(BiConsumer<String, String> bc) {
        def username = UsernameUtils.randomUsername()
        def password = PasswordUtils.randomPassword()
        def response = postRegisterNaive(username, password)
        assertEquals(response.statusCode, HttpStatus.OK)
        var respBody = response.body
        assertTrue(respBody.getBoolean("ok"))
        bc.accept(username, password)
    }

    protected ResponseEntity<JSONObject> postRegisterNaive(String username, String password) {
        var api = "${serverPrefix}/api/register/naive"
        var body = [username: username, password: password]
        return this.restTemplate.postForEntity(api, body, JSONObject.class)
    }

    protected ResponseEntity<JSONObject> postRestLogin(String username, String password) {
        var api = "${serverPrefix}/api/login"
        def body = [username: username, password: password]
        return this.restTemplate.postForEntity(api, body, JSONObject.class)
    }

    /**
     * 其正确性由 {@link #naiveRegister} 和 {@link RestLoginControllerTests} 保证。
     */
    NaiveRegisterAndRestLoginResult naiveRegisterAndRestLogin() {
        def result = new NaiveRegisterAndRestLoginResult()
        naiveRegister { username, password ->
            def resp = postRestLogin(username, password)
            assertThat(resp.statusCode).isEqualTo(HttpStatus.OK)
            def respBody = resp.body

            assertThat(respBody.get("id"))
                    .isNotNull()
                    .isInstanceOf(Number)
            result.userId = respBody.getLong("id")

            assertThat(respBody.getString("username"))
                    .isNotNull()
                    .isEqualTo(username)
            result.username = respBody.getString("username")

            assertThat(respBody.getString("displayName"))
                    .isNotNull()
                    .isEqualTo(username)
            result.displayName = respBody.getString("displayName")

            assertThat(respBody.getString("registerTime"))
                    .isNotNull()
                    .matches(DateUtils.dateTimeFormatterPatternRegExp)
                    .isLessThanOrEqualTo(LocalDateTime.now().format(DateUtils.dateTimeFormatter))

            result.cookie = resp.getHeaders().getFirst(HttpHeaders.SET_COOKIE)
        }
        return result
    }
}
