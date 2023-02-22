package run.antleg.sharp.endpoints;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import run.antleg.sharp.test.util.PasswordUtils;
import run.antleg.sharp.test.util.UsernameUtils;
import run.antleg.sharp.util.DateUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static run.antleg.sharp.modules.Facts.*;

class RestLoginControllerTests extends HttpEndpointTestsCommon {

    @Test
    void login__ok() {
        naiveRegister((username, password) -> {
            var response = postRestLogin(username, password);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            var respBody = response.getBody();
            assertThat(respBody).isNotNull();
            assertThat(respBody.get("id")).isNotNull().isInstanceOf(Number.class);
            assertThat(respBody.getString("username")).isNotNull().isEqualTo(username);
            assertThat(respBody.getString("displayName")).isNotNull().isEqualTo(username);
            assertThat(respBody.getString("registerTime")).isNotNull()
                    .matches(DateUtils.dateTimeFormatterPatternRegExp)
                    .isLessThanOrEqualTo(LocalDateTime.now().format(DateUtils.dateTimeFormatter));
        });
    }

    @Test
    void login__user_not_registered() {
        var username = UsernameUtils.randomUsername();
        var password = PasswordUtils.randomPassword();

        var response = postRestLogin(username, password);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        var respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_UNAUTHORIZED");
        assertThat(respBody.get("msg")).isNotNull().isEqualTo("用户不存在");
    }

    @Test
    void login__password_invalid() {
        naiveRegister((username, password) -> {
            var response = postRestLogin(username, StringUtils.reverse(password)/* use a wrong password */);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            var respBody = response.getBody();
            assertThat(respBody).isNotNull();
            assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_UNAUTHORIZED");
            assertThat(respBody.get("msg")).isNotNull().isEqualTo("用户名或密码错误");
        });
    }

    @Test
    void login__username_length() {
        for (int i = 1; i < 50; i++) {
            var username = UsernameUtils.randomUsernameMayBeInvalid(i);
            var password = PasswordUtils.randomPassword();
            var response = postRestLogin(username, password);
            if (i > USERNAME_MAX_LEN) {
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                var respBody = response.getBody();
                assertThat(respBody).isNotNull();
                assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_INVALID");
                assertThat(respBody.get("msg")).isNotNull().isEqualTo("用户名长度应在 1 到 24 之间");
            } else {
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                var respBody = response.getBody();
                assertThat(respBody).isNotNull();
                assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_UNAUTHORIZED");
                assertThat(respBody.get("msg")).isNotNull().isEqualTo("用户不存在");
            }
        }
    }

    @Test
    void login__password_length() {
        for (int i = 1; i < 50; i++) {
            var username = UsernameUtils.randomUsername();
            var password = PasswordUtils.randomPasswordMayBeInvalid(i);
            var response = postRestLogin(username, password);
            if (i < PASSWORD_MIN_LEN || i > PASSWORD_MAX_LEN) {
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                var respBody = response.getBody();
                assertThat(respBody).isNotNull();
                assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_INVALID");
                assertThat(respBody.get("msg")).isNotNull().isEqualTo("密码长度应在 8 到 32 之间");
            } else {
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                var respBody = response.getBody();
                assertThat(respBody).isNotNull();
                assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_UNAUTHORIZED");
                assertThat(respBody.get("msg")).isNotNull().isEqualTo("用户不存在");
            }
        }
    }
}
