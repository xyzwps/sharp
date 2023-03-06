package run.antleg.sharp.endpoints;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static run.antleg.sharp.util.CollectionUtils.mutList;
import static run.antleg.sharp.util.CollectionUtils.mutMap;
import static run.antleg.sharp.test.util.TagUtils.*;

class TagControllerTests {

    @Nested
    class $POST_tags extends ControllerTestsBase {

        @Test
        void unauthenticated() {
            given().contentType(ContentType.JSON)
                    .body(mutMap("name", randomTagName()))
                    .when().post("/api/tags")
                    .then().statusCode(HttpStatus.UNAUTHORIZED.value())
                    .body("code", is("REQUEST_UNAUTHORIZED"))
                    .body("msg", is("认证失败"));
        }

        @Test
        void ok() {
            var user = naiveRegisterAndRestLogin();
            for (int i = 0; i < 73; i++) {
                var tagName = randomTagName();
                given().contentType(ContentType.JSON)
                        .headers(cookieHeaders(user.getCookie()))
                        .body(mutMap("name", tagName))
                        .when().post("/api/tags")
                        .then().statusCode(HttpStatus.OK.value())
                        .body("name", is(tagName))
                        .body("id", isA(Number.class));
            }
        }

        @Test
        void noTagName() {
            var user = naiveRegisterAndRestLogin();
            given().contentType(ContentType.JSON)
                    .headers(cookieHeaders(user.getCookie()))
                    .body(mutMap("name", null))
                    .when().post("/api/tags")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("msg", is("标签名不能是空白"))
                    .body("code", is("REQUEST_INVALID"));
        }

        @Test
        void blankTagName() {
            var user = naiveRegisterAndRestLogin();
            mutList(null, "", " ", "     ").forEach(tagName -> given()
                    .contentType(ContentType.JSON)
                    .headers(cookieHeaders(user.getCookie()))
                    .body(mutMap("name", tagName))
                    .when().post("/api/tags")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("msg", is("标签名不能是空白"))
                    .body("code", is("REQUEST_INVALID")));
        }

        @Test
        void longTagName() {
            var user = naiveRegisterAndRestLogin();
            given()
                    .contentType(ContentType.JSON)
                    .headers(cookieHeaders(user.getCookie()))
                    .body(mutMap("name", "1234567890123456789012345"))
                    .when().post("/api/tags")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("msg", is("标签名最多包含 24 个字符"))
                    .body("code", is("REQUEST_INVALID"));
        }

        @Test
        void conflict() {
            var user = naiveRegisterAndRestLogin();
            var tagName = randomTagName();
            given()
                    .contentType(ContentType.JSON)
                    .headers(cookieHeaders(user.getCookie()))
                    .body(mutMap("name", tagName))
                    .when().post("/api/tags")
                    .then().statusCode(HttpStatus.OK.value())
                    .body("name", is(tagName))
                    .body("id", isA(Number.class));
            // again and no conflict
            given()
                    .contentType(ContentType.JSON)
                    .headers(cookieHeaders(user.getCookie()))
                    .body(mutMap("name", tagName))
                    .when().post("/api/tags")
                    .then().statusCode(HttpStatus.OK.value())
                    .body("name", is(tagName))
                    .body("id", isA(Number.class));
        }

        @Test
        void unsupportedTagName() {
            var user = naiveRegisterAndRestLogin();
            given()
                    .contentType(ContentType.JSON)
                    .headers(cookieHeaders(user.getCookie()))
                    .body(mutMap("name", "unsupported"))
                    .when().post("/api/tags")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("msg", is("暂不支持此标签"))
                    .body("code", is("TAG_UNSUPPORTED"));
        }
    }
}
