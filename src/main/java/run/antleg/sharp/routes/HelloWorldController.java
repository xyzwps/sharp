package run.antleg.sharp.routes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/hello")
public class HelloWorldController {

    @GetMapping("/world")
    public Map<String, Object> get() {
        return Map.of("ddd", "dd");
    }
}
