package run.antleg.sharp.endpoints

import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import run.antleg.sharp.util.DateUtils
import run.antleg.sharp.util.JSONObject

import java.time.LocalDateTime

import static org.assertj.core.api.Assertions.*

class UserControllerTests extends HttpEndpointTestsCommon {

    private ResponseEntity<JSONObject> getCurrentUser(String cookie) {
        var api = "${serverPrefix}/api/users/current"
        def headers = new HttpHeaders()
        if (cookie) {
            headers.set(HttpHeaders.COOKIE, cookie)
        }
        def request = new HttpEntity(headers)
        return this.restTemplate.exchange(api, HttpMethod.GET, request, JSONObject.class)
    }

    @Test
    void "current user info - unauthenticated"() {
        def response = getCurrentUser(null)
        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        def respBody = response.body
        assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_FORBIDDEN")
        assertThat(respBody.get("msg")).isNotNull().isEqualTo("未授权")
    }

    @Test
    void "current user info - authenticated"() {
        def result = naiveRegisterAndRestLogin()

        def response = getCurrentUser(result.cookie)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        def respBody = response.body
        assertThat(respBody.get("id"))
                .isNotNull()
                .isEqualTo(result.userId)
        assertThat(respBody.getString("username"))
                .isNotNull()
                .isEqualTo(result.username)
        assertThat(respBody.getString("displayName"))
                .isNotNull()
                .isEqualTo(result.displayName)
        assertThat(respBody.getString("registerTime"))
                .isNotNull()
                .matches(DateUtils.dateTimeFormatterPatternRegExp)
                .isLessThanOrEqualTo(LocalDateTime.now().format(DateUtils.dateTimeFormatter))
    }

}
