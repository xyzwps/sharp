package run.antleg.sharp.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class Servlets {

    public static void sendJson(HttpServletResponse response, HttpStatus status, Object body) {
        sendJson(response, status, null, body);
    }

    public static void sendJson(HttpServletResponse response, HttpStatus status, Map<String, String> headers, Object body) {
        try {
            response.setStatus(status.value());
            response.setHeader("Content-Type", APPLICATION_JSON_VALUE);
            if (headers != null) headers.forEach(response::setHeader);
            var os = response.getOutputStream();
            JSON.OBJECT_MAPPER.writeValue(os, body);
            os.flush();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
