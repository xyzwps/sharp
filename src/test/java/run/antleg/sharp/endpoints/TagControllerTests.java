package run.antleg.sharp.endpoints;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static run.antleg.sharp.util.CollectionUtils.mutMap;

public class TagControllerTests extends ControllerTestsBase {

//    @Test
//    public void $post_todos___unauthenticated() {
//        var todoId = generateTodoId();
//        given().contentType(ContentType.JSON)
//                .body(mutMap("todoId", todoId, "details", "whatever", "status", "TODO"))
//                .when().post("/api/todos")
//                .then().statusCode(HttpStatus.UNAUTHORIZED.value())
//                .body("code", is("REQUEST_UNAUTHORIZED"))
//                .body("msg", is("认证失败"));
//    }

}
