package run.antleg.sharp.endpoints

import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import run.antleg.sharp.test.util.PasswordUtils
import run.antleg.sharp.test.util.UsernameUtils
import run.antleg.sharp.util.JSONObject

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

import static run.antleg.sharp.modules.Facts.*

class RegisterControllerTests extends HttpEndpointTestsCommon {


    private ResponseEntity<JSONObject> postRegisterNaive(Object requestBody) {
        var api = "${serverPrefix}/api/register/naive"
        return this.restTemplate.postForEntity(api, requestBody, JSONObject.class)
    }

    @Test
    void "naive register - simple"() {
        def response = postRegisterNaive([
                username: UsernameUtils.randomUsername(),
                password: PasswordUtils.randomPassword()
        ])
        assertEquals(response.statusCode, HttpStatus.OK)
        var respBody = response.body
        assertTrue(respBody.getBoolean("ok"))
    }

    @Test
    void "naive register - check password length"() {
        for (i in 1..<50) {
            def response = postRegisterNaive([
                    username: UsernameUtils.randomUsername(),
                    password: PasswordUtils.randomPasswordMayBeInvalid(i)
            ])
            if (i < PASSWORD_MIN_LEN || i > PASSWORD_MAX_LEN) {
                assertEquals(response.statusCode, HttpStatus.BAD_REQUEST)
                var respBody = response.body
                assertEquals(respBody.getString("code"), "REQUEST_INVALID")
                assertEquals(respBody.getString("msg"), "密码长度应在 8 到 32 个字符之间")
            } else {
                assertEquals(response.statusCode, HttpStatus.OK)
                var respBody = response.body
                assertTrue(respBody.getBoolean("ok"))
            }
        }
    }

    @Test
    void "naive register - no password"() {
        def response = postRegisterNaive([
                username: UsernameUtils.randomUsername()
        ])
        assertEquals(response.statusCode, HttpStatus.BAD_REQUEST)
        var respBody = response.body
        assertEquals(respBody.getString("code"), "REQUEST_INVALID")
        assertEquals(respBody.getString("msg"), "缺少密码")
    }

    @Test
    void "naive register - check username length"() {
        for (i in 1..<50) {
            def response = postRegisterNaive([
                    username: UsernameUtils.randomUsernameMayBeInvalid(i),
                    password: PasswordUtils.randomPassword()
            ])
            if (i < USERNAME_MIN_LEN || i > USERNAME_MAX_LEN) {
                assertEquals(response.statusCode, HttpStatus.BAD_REQUEST)
                var respBody = response.body
                assertEquals(respBody.getString("code"), "REQUEST_INVALID")
                assertEquals(respBody.getString("msg"), "用户名长度应在 1 到 24 个字符之间")
            } else {
                assertEquals(response.statusCode, HttpStatus.OK)
                var respBody = response.body
                assertTrue(respBody.getBoolean("ok"))
            }
        }
    }

    @Test
    void "naive register - no username"() {
        def response = postRegisterNaive([
                password: PasswordUtils.randomPassword()
        ])
        assertEquals(response.statusCode, HttpStatus.BAD_REQUEST)
        var respBody = response.body
        assertEquals(respBody.getString("code"), "REQUEST_INVALID")
        assertEquals(respBody.getString("msg"), "缺少用户名")
    }

    @Test
    void "naive register - username used"() {
        def username = UsernameUtils.randomUsername()

        round1: {
            def response = postRegisterNaive([
                    username: username,
                    password: PasswordUtils.randomPassword()
            ])
            assertEquals(response.statusCode, HttpStatus.OK)
            var respBody = response.body
            assertTrue(respBody.getBoolean("ok"))
        }

        round2: {
            def response = postRegisterNaive([
                    username: username,
                    password: PasswordUtils.randomPassword()
            ])
            assertEquals(response.statusCode, HttpStatus.BAD_REQUEST)
            var respBody = response.body
            assertEquals(respBody.getString("code"), "USERNAME_CONFLICT")
            assertEquals(respBody.getString("msg"), "用户名已存在")
        }
    }
}
