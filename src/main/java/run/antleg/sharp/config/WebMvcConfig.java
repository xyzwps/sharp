package run.antleg.sharp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import run.antleg.sharp.config.converter.StringToLongRecordConverter;
import run.antleg.sharp.config.converter.StringToStringRecordConverter;
import run.antleg.sharp.modules.anthology.model.AnthologyId;
import run.antleg.sharp.modules.anthology.model.AuthorId;
import run.antleg.sharp.modules.todo.model.TodoId;
import run.antleg.sharp.modules.user.model.UserId;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        new StringToLongRecordConverter<>(UserId.class, UserId::new).install(registry);

        new StringToLongRecordConverter<>(AuthorId.class, AuthorId::new).install(registry);
        new StringToStringRecordConverter<>(AnthologyId.class, AnthologyId::new).install(registry);

        new StringToStringRecordConverter<>(TodoId.class, TodoId::new).install(registry);
    }
}
