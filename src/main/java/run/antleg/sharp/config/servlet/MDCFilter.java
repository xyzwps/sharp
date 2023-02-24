package run.antleg.sharp.config.servlet;

import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import run.antleg.sharp.modules.Facts;

import java.io.IOException;
import java.util.Optional;

@WebFilter
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MDCFilter implements Filter {

    public static final String MDC_REQUEST_ID_KEY = "requestId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            insertIntoMDC(request);
            chain.doFilter(request, response);
        } finally {
            clearMDC();
        }
    }

    public static Optional<String> getRequestId() {
        return Optional.ofNullable(MDC.get(MDC_REQUEST_ID_KEY)).filter(it -> !it.isBlank());
    }

    private static void insertIntoMDC(ServletRequest request) {
        if (request instanceof HttpServletRequest req) {
            String reqId = req.getHeader(Facts.HEADER_X_REQUEST_ID);
            if (reqId == null || reqId.isBlank()) {
                reqId = UlidCreator.getUlid().toString();
            }
            MDC.put(MDC_REQUEST_ID_KEY, reqId);
        }
    }

    private static void clearMDC() {
        MDC.remove(MDC_REQUEST_ID_KEY);
    }
}
