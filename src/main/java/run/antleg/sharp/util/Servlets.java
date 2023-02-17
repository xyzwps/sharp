package run.antleg.sharp.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.UncheckedIOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class Servlets {

    public static void sendJson(HttpServletResponse response, HttpStatus status, Object body) {
        try {
            response.setStatus(status.value());
            response.setHeader("Content-Type", APPLICATION_JSON_VALUE);
            var os = response.getOutputStream();
            JSON.OBJECT_MAPPER.writeValue(os, body);
            os.flush();
            os.close();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
