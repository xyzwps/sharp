package run.antleg.sharp.endpoints;

import org.junit.jupiter.api.Test;
import run.antleg.sharp.modules.OK;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DemoControllerTests extends HttpEndpointTestsCommon {

    @Test
    void demo() {
        assertTrue(this.restTemplate.getForObject(serverPrefix + "/demo", OK.class).isOk());
    }
}
