package run.antleg.sharp.endpoints

import org.junit.ClassRule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ActiveProfiles
import run.antleg.sharp.test.tc.Mysql

@ActiveProfiles("tc")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class HttpEndpointTestsCommon {

    private static final int port = 41592

    @Autowired
    TestRestTemplate restTemplate

    static String getServerPrefix() {
        return "http://localhost:${port}"
    }

    @ClassRule
    static final Mysql mysql = Mysql.instance
}
