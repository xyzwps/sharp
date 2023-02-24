package run.antleg.sharp.endpoints;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import run.antleg.sharp.util.DateUtils;
import run.antleg.sharp.util.JSONObject;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static run.antleg.sharp.util.CollectionUtils.*;

class UserControllerTests extends ControllerTestsBase {

    private ResponseEntity<JSONObject> getCurrentUser(String cookie) {
        var api = serverPrefix + "/api/users/current";
        var headers = new HttpHeaders();
        if (cookie != null && !cookie.isBlank()) {
            headers.set(HttpHeaders.COOKIE, cookie);
        }
        var request = new HttpEntity<>(headers);
        return this.restTemplate.exchange(api, HttpMethod.GET, request, JSONObject.class);
    }

    private ResponseEntity<JSONObject> getUserById(Integer userId, String cookie) {
        var api = serverPrefix + "/api/users/" + userId;
        var headers = new HttpHeaders();
        if (cookie != null && !cookie.isBlank()) {
            headers.set(HttpHeaders.COOKIE, cookie);
        }
        var request = new HttpEntity<>(headers);
        return this.restTemplate.exchange(api, HttpMethod.GET, request, JSONObject.class);
    }

    private ResponseEntity<JSONObject> updateUserDisplayName(String displayName, String cookie) {
        var api = serverPrefix + "/api/users/current";
        var headers = new HttpHeaders();
        if (cookie != null && !cookie.isBlank()) {
            headers.set(HttpHeaders.COOKIE, cookie);
        }
        var body = mutMap("displayName", displayName);
        var request = new HttpEntity<>(body, headers);
        return this.restTemplate.exchange(api, HttpMethod.PATCH, request, JSONObject.class);
    }

    @Test
    void current_user_info__unauthenticated() {
        var response = getCurrentUser(null);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        var respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_FORBIDDEN");
        assertThat(respBody.get("msg")).isNotNull().isEqualTo("未授权");
    }

    @Test
    void current_user_info__authenticated() {
        var result = naiveRegisterAndRestLogin();

        var response = getCurrentUser(result.getCookie());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.get("id")).isNotNull().isEqualTo(result.getUserId());
        assertThat(respBody.getString("username")).isNotNull().isEqualTo(result.getUsername());
        assertThat(respBody.getString("displayName")).isNotNull().isEqualTo(result.getDisplayName());
        assertThat(respBody.getString("registerTime")).isNotNull()
                .matches(DateUtils.dateTimeFormatterPatternRegExp)
                .isLessThanOrEqualTo(LocalDateTime.now().format(DateUtils.dateTimeFormatter));
    }

    @Test
    void get_any_user_info__unauthenticated() {
        var result = naiveRegisterAndRestLogin();

        var response = getUserById(result.getUserId(), null);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.get("id")).isNotNull().isEqualTo(result.getUserId());
        assertThat(respBody.getString("username")).isNotNull().isEqualTo(result.getUsername());
        assertThat(respBody.getString("displayName")).isNotNull().isEqualTo(result.getDisplayName());
        assertThat(respBody.getString("registerTime")).isNotNull()
                .matches(DateUtils.dateTimeFormatterPatternRegExp)
                .isLessThanOrEqualTo(LocalDateTime.now().format(DateUtils.dateTimeFormatter));
    }

    @Test
    void get_any_user_info__authenticated() {
        var result = naiveRegisterAndRestLogin();
        var result2 = naiveRegisterAndRestLogin();

        var response = getUserById(result.getUserId(), result2.getCookie());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.get("id")).isNotNull().isEqualTo(result.getUserId());
        assertThat(respBody.getString("username")).isNotNull().isEqualTo(result.getUsername());
        assertThat(respBody.getString("displayName")).isNotNull().isEqualTo(result.getDisplayName());
        assertThat(respBody.getString("registerTime")).isNotNull()
                .matches(DateUtils.dateTimeFormatterPatternRegExp)
                .isLessThanOrEqualTo(LocalDateTime.now().format(DateUtils.dateTimeFormatter));
    }

    @Test
    void update_displayName__unauthenticated() {
        var response = updateUserDisplayName("刻晴", null);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        var respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_FORBIDDEN");
        assertThat(respBody.get("msg")).isNotNull().isEqualTo("未授权");
    }

    @Test
    void update_displayName__authenticated() {
        var result = naiveRegisterAndRestLogin();

        var response = updateUserDisplayName("刻晴", result.getCookie());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.get("id")).isNotNull().isEqualTo(result.getUserId());
        assertThat(respBody.getString("username")).isNotNull().isEqualTo(result.getUsername());
        assertThat(respBody.getString("displayName")).isNotNull().isEqualTo("刻晴");
    }

    @Test
    void update_displayName__very_long() {
        var result = naiveRegisterAndRestLogin();

        var response = updateUserDisplayName("刻晴".repeat(21), result.getCookie());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        var respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_INVALID");
        assertThat(respBody.get("msg")).isNotNull().isEqualTo("昵称不可超过 40 个字符");
    }

    @Test
    void update_displayName__blank() {
        var result = naiveRegisterAndRestLogin();

        var response = updateUserDisplayName("  ", result.getCookie());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        var respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.get("code")).isNotNull().isEqualTo("REQUEST_INVALID");
        assertThat(respBody.get("msg")).isNotNull().isEqualTo("缺少昵称");
    }
}
