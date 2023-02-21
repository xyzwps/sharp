package run.antleg.sharp.endpoints

import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

class RegisterControllerTests extends HttpEndpointTestsCommon {

    @Test
    void "naive register - simple"() {
        var requestBody = [username: "楚仪", password: "chuyi"]
        var api = "${serverPrefix}/api/register/naive"
        var response = this.restTemplate.postForEntity(api, requestBody, Map.class)
        assertEquals(response.statusCode, HttpStatus.OK)
        var respBody = response.body
        assertTrue(respBody.ok)
    }
}
