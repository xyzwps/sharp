package run.antleg.sharp.endpoints;

import com.github.f4b6a3.ulid.UlidCreator;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import run.antleg.sharp.modules.Facts;
import run.antleg.sharp.util.DateUtils;

import java.util.List;
import java.util.Optional;

import static run.antleg.sharp.util.CollectionUtils.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TodoControllerTests extends ControllerTestsBase {

    @Test
    public void create_get_update_get() {
        var user = naiveRegisterAndRestLogin();

        var todoId = generateTodoId();
        createTodoOK(user.getCookie(), CreateTodoRequest.builder()
                .todoId(todoId).details("陪刻晴练云来古剑").status("TODO")
                .build());

        getTodoByIdOK(user.getCookie(), todoId, "陪刻晴练云来古剑", "TODO");

        patchTodoOK(user.getCookie(), todoId, PatchTodoRequest.builder()
                .details("正在练云来古剑").status("IN_PROGRESS")
                .build());

        getTodoByIdOK(user.getCookie(), todoId, "正在练云来古剑", "IN_PROGRESS");
    }

    @Test
    public void state_transitions() {
        var user = naiveRegisterAndRestLogin();

        var stats = List.of("TODO", "IN_PROGRESS", "DONE", "DELETED");
        // 当前任意两个状态之间可以转换
        for (int i = 0; i < stats.size(); i++) {
            var state0 = stats.get(i);
            for (int j = 0; j < stats.size(); j++) {
                var state1 = stats.get(i);

                var todoId = generateTodoId();
                createTodoOK(user.getCookie(), CreateTodoRequest.builder()
                        .todoId(todoId).details("爬群玉阁").status(state0)
                        .build());

                patchTodoOK(user.getCookie(), todoId, PatchTodoRequest.builder()
                        .details("爬群玉阁").status(state1)
                        .build());
            }
        }
    }

    @Test
    public void $post_todos___unauthenticated() {
        var todoId = generateTodoId();
        given().contentType(ContentType.JSON)
                .body(mutMap("todoId", todoId, "details", "whatever", "status", "TODO"))
                .when().post("/api/todos")
                .then().statusCode(HttpStatus.FORBIDDEN.value())
                .body("code", is("REQUEST_FORBIDDEN"))
                .body("msg", is("未授权"));
    }

    @Test
    public void $post_todos___todo_id_missing() {
        var user = naiveRegisterAndRestLogin();
        given().contentType(ContentType.JSON).headers(cookieHeaders(user.getCookie()))
                .body(CreateTodoRequest.builder()
                        .details("whatever").status("TODO")
                        .build())
                .when().post("/api/todos")
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", is("REQUEST_INVALID"))
                .body("msg", is("缺少 todoId"));
    }

    @Test
    public void $post_todos___todo_id_length() {
        var user = naiveRegisterAndRestLogin();
        for (int len = 0; len < 50; len++) {
            var body = CreateTodoRequest.builder()
                    .details("whatever").status("TODO").todoId("1".repeat(len))
                    .build();
            if (len < Facts.TODO_ID_MIN_LEN || len > Facts.TODO_ID_MAX_LEN) {
                given().contentType(ContentType.JSON)
                        .headers(cookieHeaders(user.getCookie()))
                        .body(body)
                        .when().post("/api/todos")
                        .then().statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("code", is("REQUEST_INVALID"))
                        .body("msg", is("TodoId 应包含 14 到 36 个字符"));
            } else {
                createTodoOK(user.getCookie(), body);
            }
        }
    }

    @Test
    public void $post_todos___todo_id_pattern() {
        var user = naiveRegisterAndRestLogin();
        var body = CreateTodoRequest.builder()
                .details("whatever").status("TODO").todoId("1234567890123456+")
                .build();
        given().contentType(ContentType.JSON)
                .headers(cookieHeaders(user.getCookie()))
                .body(body)
                .when().post("/api/todos")
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", is("REQUEST_INVALID"))
                .body("msg", is("TodoId 只能包含大小写字母、数字和 '_'、'-'、'.'"));
    }

    @Test
    public void $post_todos___details_blank() {
        var user = naiveRegisterAndRestLogin();

        var cases = mutList("", "  ", null);
        for (var details : cases) {
            var body = CreateTodoRequest.builder()
                    .details(details).status("TODO").todoId(generateTodoId())
                    .build();
            given().contentType(ContentType.JSON)
                    .headers(cookieHeaders(user.getCookie()))
                    .body(body)
                    .when().post("/api/todos")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", is("REQUEST_INVALID"))
                    .body("msg", is("待办内容不能是空白"));
        }
    }

    @Test
    public void $post_todos___details_length() {
        var user = naiveRegisterAndRestLogin();

        for (int i = 1; i < 140; i++) {
            var body = CreateTodoRequest.builder()
                    .details("1".repeat(i)).status("TODO").todoId(generateTodoId())
                    .build();
            if (i <= 120) {
                createTodoOK(user.getCookie(), body);
            } else {
                given().contentType(ContentType.JSON)
                        .headers(cookieHeaders(user.getCookie()))
                        .body(body)
                        .when().post("/api/todos")
                        .then().statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("code", is("REQUEST_INVALID"))
                        .body("msg", is("待办内容不可超过 120 个字符"));
            }
        }
    }

    @Test
    public void $post_todos___status_missing() {
        var user = naiveRegisterAndRestLogin();

        var body = CreateTodoRequest.builder()
                .details("1").todoId(generateTodoId())
                .build();
        given().contentType(ContentType.JSON)
                .headers(cookieHeaders(user.getCookie()))
                .body(body)
                .when().post("/api/todos")
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", is("REQUEST_INVALID"))
                .body("msg", is("缺少 status"));
    }

    @Test
    public void $post_todos___status_invalid() {
        var user = naiveRegisterAndRestLogin();

        var body = CreateTodoRequest.builder()
                .details("1").todoId(generateTodoId()).status("WILL_DO")
                .build();
        given().contentType(ContentType.JSON)
                .headers(cookieHeaders(user.getCookie()))
                .body(body)
                .when().post("/api/todos")
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", is("REQUEST_INVALID"))
                .body("msg", is("不正确的 status"));
    }

    @Test
    public void $get_todos_$todoId___unauthenticated() {
        var todoId = generateTodoId();
        when().get("/api/todos/" + todoId)
                .then().statusCode(HttpStatus.FORBIDDEN.value())
                .body("code", is("REQUEST_FORBIDDEN"))
                .body("msg", is("未授权"));
    }

    @Test
    public void $get_todos___unauthenticated() {
        when().get("/api/todos")
                .then().statusCode(HttpStatus.FORBIDDEN.value())
                .body("code", is("REQUEST_FORBIDDEN"))
                .body("msg", is("未授权"));
    }

    @Test
    public void $patch_todos_$todoId___unauthenticated() {
        var todoId = generateTodoId();
        given().contentType(ContentType.JSON)
                .body(mutMap("details", "whatever", "status", "TODO"))
                .when().patch("/api/todos/" + todoId)
                .then().statusCode(HttpStatus.FORBIDDEN.value())
                .body("code", is("REQUEST_FORBIDDEN"))
                .body("msg", is("未授权"));
    }

    @Test
    public void $patch_todos_$todoId___details_length() {
        var user = naiveRegisterAndRestLogin();
        var todoId = generateTodoId();
        createTodoOK(user.getCookie(), CreateTodoRequest.builder()
                .details("whatever").status("TODO").todoId(todoId)
                .build());

        for (int i = 1; i < 140; i++) {
            var body = PatchTodoRequest.builder()
                    .details("1".repeat(i)).status("TODO")
                    .build();
            if (i <= 120) {
                patchTodoOK(user.getCookie(), todoId, body);
            } else {
                given().contentType(ContentType.JSON)
                        .headers(cookieHeaders(user.getCookie()))
                        .body(body)
                        .when().patch("/api/todos/" + todoId)
                        .then().statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("code", is("REQUEST_INVALID"))
                        .body("msg", is("待办内容不可超过 120 个字符"));
            }
        }
    }

    @Test
    public void $patch_todos_$todoId___details_blank() {
        var user = naiveRegisterAndRestLogin();
        var todoId = generateTodoId();
        createTodoOK(user.getCookie(), CreateTodoRequest.builder()
                .details("whatever").status("TODO").todoId(todoId)
                .build());

        var cases = mutList("", "  ", null);
        for (var details : cases) {
            var body = PatchTodoRequest.builder()
                    .details(details).status("TODO")
                    .build();
            given().contentType(ContentType.JSON)
                    .headers(cookieHeaders(user.getCookie()))
                    .body(body)
                    .when().patch("/api/todos/" + todoId)
                    .then().statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", is("REQUEST_INVALID"))
                    .body("msg", is("待办内容不能是空白"));
        }
    }

    @Test
    public void $patch_todos_$todoId___status_missing() {
        var user = naiveRegisterAndRestLogin();
        var todoId = generateTodoId();
        createTodoOK(user.getCookie(), CreateTodoRequest.builder()
                .details("whatever").status("TODO").todoId(todoId)
                .build());

        var body = PatchTodoRequest.builder()
                .details("1")
                .build();
        given().contentType(ContentType.JSON)
                .headers(cookieHeaders(user.getCookie()))
                .body(body)
                .when().patch("/api/todos/" + todoId)
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", is("REQUEST_INVALID"))
                .body("msg", is("缺少 status"));
    }

    @Test
    public void $patch_todos_$todoId___status_invalid() {
        var user = naiveRegisterAndRestLogin();
        var todoId = generateTodoId();
        createTodoOK(user.getCookie(), CreateTodoRequest.builder()
                .details("whatever").status("TODO").todoId(todoId)
                .build());

        var body = PatchTodoRequest.builder()
                .details("1").status("I_DO")
                .build();
        given().contentType(ContentType.JSON)
                .headers(cookieHeaders(user.getCookie()))
                .body(body)
                .when().patch("/api/todos/" + todoId)
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", is("REQUEST_INVALID"))
                .body("msg", is("不正确的 status"));
    }

    private static String generateTodoId() {
        return UlidCreator.getUlid().toString();
    }


    private void createTodoOK(String cookie, CreateTodoRequest body) {
        given().contentType(ContentType.JSON).headers(cookieHeaders(cookie)).body(body)
                .when().post("/api/todos")
                .then().statusCode(HttpStatus.OK.value())
                .body("id", is(body.todoId))
                .body("details", is(body.details))
                .body("status", is(body.status))
                .body("createTime", matchesPattern(DateUtils.dateTimeFormatterPatternRegExp))
                .body("createTime", lessThanOrEqualTo(DateUtils.dateTimeNowString()))
                .body("updateTime", matchesPattern(DateUtils.dateTimeFormatterPatternRegExp))
                .body("updateTime", lessThanOrEqualTo(DateUtils.dateTimeNowString()));
    }

    private static Headers cookieHeaders(String cookie) {
        return Optional.ofNullable(cookie)
                .filter(it -> !it.isBlank())
                .map(it -> Headers.headers(new Header(HttpHeaders.COOKIE, cookie)))
                .orElseGet(Headers::new);
    }

    private void getTodoByIdOK(String cookie, String todoId, String details, String status) {
        given().headers(cookieHeaders(cookie))
                .when().get("/api/todos/" + todoId)
                .then().statusCode(HttpStatus.OK.value())
                .body("id", is(todoId))
                .body("details", is(details))
                .body("status", is(status))
                .body("createTime", matchesPattern(DateUtils.dateTimeFormatterPatternRegExp))
                .body("createTime", lessThanOrEqualTo(DateUtils.dateTimeNowString()))
                .body("updateTime", matchesPattern(DateUtils.dateTimeFormatterPatternRegExp))
                .body("updateTime", lessThanOrEqualTo(DateUtils.dateTimeNowString()));
    }

    private void patchTodoOK(String cookie, String todoId, PatchTodoRequest body) {
        given().contentType(ContentType.JSON).headers(cookieHeaders(cookie)).body(body)
                .when().patch("/api/todos/" + todoId)
                .then().statusCode(HttpStatus.OK.value())
                .body("id", is(todoId))
                .body("details", is(body.details))
                .body("status", is(body.status))
                .body("createTime", matchesPattern(DateUtils.dateTimeFormatterPatternRegExp))
                .body("createTime", lessThanOrEqualTo(DateUtils.dateTimeNowString()))
                .body("updateTime", matchesPattern(DateUtils.dateTimeFormatterPatternRegExp))
                .body("updateTime", lessThanOrEqualTo(DateUtils.dateTimeNowString()));
    }

    /**
     * @see run.antleg.sharp.modules.todo.command.CreateTodoCommand
     */
    @Builder
    @Data
    private static class CreateTodoRequest {
        private String todoId;
        private String details;
        private String status;
    }

    /**
     * @see run.antleg.sharp.modules.todo.command.PatchTodoCommand
     */
    @Builder
    @Data
    private static class PatchTodoRequest {
        private String details;
        private String status;
    }

}
