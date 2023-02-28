package run.antleg.sharp.modules;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public OK ok() {
        return OK.INSTANCE;
    }
}
