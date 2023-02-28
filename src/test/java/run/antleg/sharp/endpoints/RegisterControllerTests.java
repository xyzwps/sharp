package run.antleg.sharp.endpoints;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import run.antleg.sharp.test.util.PasswordUtils;
import run.antleg.sharp.test.util.UsernameUtils;

import static org.junit.jupiter.api.Assertions.*;

import static run.antleg.sharp.modules.Facts.*;

class RegisterControllerTests extends ControllerTestsBase {

    @Test
    void naive_register__simple() {
        var response = postRegisterNaive(UsernameUtils.randomUsername(), PasswordUtils.randomPassword());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        var respBody = response.getBody();
        assertNotNull(respBody);
        assertTrue(respBody.getBoolean("ok"));
    }

    @Test
    void naive_register__check_password_length() {
        for (int i = 1; i < 50; i++) {
            var response = postRegisterNaive(UsernameUtils.randomUsername(), PasswordUtils.randomPasswordMayBeInvalid(i));
            if (i < PASSWORD_MIN_LEN || i > PASSWORD_MAX_LEN) {
                assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
                var respBody = response.getBody();
                assertNotNull(respBody);
                assertEquals(respBody.getString("code"), "REQUEST_INVALID");
                assertEquals(respBody.getString("msg"), "密码长度应在 8 到 32 个字符之间");
            } else {
                assertEquals(response.getStatusCode(), HttpStatus.OK);
                var respBody = response.getBody();
                assertNotNull(respBody);
                assertTrue(respBody.getBoolean("ok"));
            }
        }
    }

    @Test
    void naive_register__no_password() {
        var response = postRegisterNaive(UsernameUtils.randomUsername(), null);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        var respBody = response.getBody();
        assertNotNull(respBody);
        assertEquals(respBody.getString("code"), "REQUEST_INVALID");
        assertEquals(respBody.getString("msg"), "缺少密码");
    }

    @Test
    void naive_register__check_username_length() {
        for (int i = 1; i < 50; i++) {
            var response = postRegisterNaive(UsernameUtils.randomUsernameMayBeInvalid(i), PasswordUtils.randomPassword());
            if (i > USERNAME_MAX_LEN) {
                assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
                var respBody = response.getBody();
                assertNotNull(respBody);
                assertEquals(respBody.getString("code"), "REQUEST_INVALID");
                assertEquals(respBody.getString("msg"), "用户名长度应在 1 到 24 个字符之间");
            } else {
                assertEquals(response.getStatusCode(), HttpStatus.OK);
                var respBody = response.getBody();
                assertNotNull(respBody);
                assertTrue(respBody.getBoolean("ok"));
            }
        }
    }

    @Test
    void naive_register__no_username() {
        var response = postRegisterNaive(null, PasswordUtils.randomPassword());
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        var respBody = response.getBody();
        assertNotNull(respBody);
        assertEquals(respBody.getString("code"), "REQUEST_INVALID");
        assertEquals(respBody.getString("msg"), "缺少用户名");
    }

    @Test
    void naive_register__username_used() {
        var username = UsernameUtils.randomUsername();

        // 先把用户名给注册了
        {
            var response = postRegisterNaive(username, PasswordUtils.randomPassword());
            assertEquals(response.getStatusCode(), HttpStatus.OK);
            var respBody = response.getBody();
            assertNotNull(respBody);
            assertTrue(respBody.getBoolean("ok"));
        }

        // 用已经注册过的用户名再注册一次试试
        {
            var response = postRegisterNaive(username, PasswordUtils.randomPassword());
            assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
            var respBody = response.getBody();
            assertNotNull(respBody);
            assertEquals(respBody.getString("code"), "USERNAME_CONFLICT");
            assertEquals(respBody.getString("msg"), "用户名已存在");
        }
    }
}
