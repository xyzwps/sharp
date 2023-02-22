package run.antleg.sharp.endpoints

import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import run.antleg.sharp.test.util.PasswordUtils
import run.antleg.sharp.test.util.UsernameUtils
import run.antleg.sharp.util.DateUtils
import run.antleg.sharp.util.JSONObject

import java.time.LocalDateTime

import static org.assertj.core.api.Assertions.*
import static run.antleg.sharp.modules.Facts.*

class RestLoginControllerTests extends HttpEndpointTestsCommon {

    private ResponseEntity<JSONObject> postLogin(String username, String password) {
        var api = "${serverPrefix}/api/login"
        def body = [username: username, password: password]
        return this.restTemplate.postForEntity(api, body, JSONObject.class)
    }

    @Test
    void "login - ok"() {
        naiveRegister { username, password ->
            def response = postLogin(username, password)
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            def respBody = response.body
            assertThat(respBody.get("id"))
                    .isNotNull()
                    .isInstanceOf(Number)
            assertThat(respBody.getString("username"))
                    .isNotNull()
                    .isEqualTo(username)
            assertThat(respBody.getString("displayName"))
                    .isNotNull()
                    .isEqualTo(username)
            assertThat(respBody.getString("registerTime"))
                    .isNotNull()
                    .matches(DateUtils.dateTimeFormatterPatternRegExp)
                    .isLessThanOrEqualTo(LocalDateTime.now().format(DateUtils.dateTimeFormatter))
        }
    }

    @Test
    void "login - user not registered"() {
        def username = UsernameUtils.randomUsername()
        def password = PasswordUtils.randomPassword()

        def response = postLogin(username, password)
        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        def respBody = response.body
        assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_UNAUTHORIZED")
        assertThat(respBody.get("msg")).isNotNull().isEqualTo("用户不存在")
    }

    @Test
    void "login - password invalid"() {
        naiveRegister { username, password ->
            def response = postLogin(username, password.reverse()/* use a wrong password */)
            assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
            def respBody = response.body
            assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_UNAUTHORIZED")
            assertThat(respBody.get("msg")).isNotNull().isEqualTo("用户名或密码错误")
        }
    }

    @Test
    void "login - username length"() {
        for (i in 1..<50) {
            def username = UsernameUtils.randomUsernameMayBeInvalid(i)
            def password = PasswordUtils.randomPassword()
            def response = postLogin(username, password)
            if (i < USERNAME_MIN_LEN || i > USERNAME_MAX_LEN) {
                assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
                def respBody = response.body
                assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_INVALID")
                assertThat(respBody.get("msg")).isNotNull().isEqualTo("用户名长度应在 1 到 24 之间")
            } else {
                assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
                def respBody = response.body
                assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_UNAUTHORIZED")
                assertThat(respBody.get("msg")).isNotNull().isEqualTo("用户不存在")
            }
        }
    }

    @Test
    void "login - password length"() {
        for (i in 1..<50) {
            def username = UsernameUtils.randomUsername()
            def password = PasswordUtils.randomPasswordMayBeInvalid(i)
            def response = postLogin(username, password)
            if (i < PASSWORD_MIN_LEN || i > PASSWORD_MAX_LEN) {
                assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
                def respBody = response.body
                assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_INVALID")
                assertThat(respBody.get("msg")).isNotNull().isEqualTo("密码长度应在 8 到 32 之间")
            } else {
                assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
                def respBody = response.body
                assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_UNAUTHORIZED")
                assertThat(respBody.get("msg")).isNotNull().isEqualTo("用户不存在")
            }
        }
    }
}
