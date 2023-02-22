package run.antleg.sharp.endpoints

import org.junit.ClassRule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import run.antleg.sharp.test.tc.Mysql
import run.antleg.sharp.test.util.PasswordUtils
import run.antleg.sharp.test.util.UsernameUtils
import run.antleg.sharp.util.JSONObject

import java.util.function.BiConsumer
import java.util.function.Consumer

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

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
}
