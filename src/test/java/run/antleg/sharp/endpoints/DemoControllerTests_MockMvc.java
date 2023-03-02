package run.antleg.sharp.endpoints;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import run.antleg.sharp.modules.Facts;

@SpringBootTest
@AutoConfigureMockMvc
public class DemoControllerTests_MockMvc {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void mockMvcDemo() throws Exception {
        this.mockMvc.perform(get("/demo")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist(Facts.HEADER_X_REQUEST_ID))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ok").value(true));
    }
}