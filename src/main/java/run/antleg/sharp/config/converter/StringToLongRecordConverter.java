package run.antleg.sharp.config.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;

import java.util.Objects;
import java.util.function.Function;

public class StringToLongRecordConverter<LR> implements Converter<String, LR> {

    private final Function<Long, LR> constructor;

    private final Class<LR> lrClass;

    public StringToLongRecordConverter(Class<LR> lrClass, Function<Long, LR> constructor) {
        this.lrClass = Objects.requireNonNull(lrClass);
        this.constructor = Objects.requireNonNull(constructor);
    }

    @Override
    public LR convert(@NonNull String it) {
        return constructor.apply(Long.parseLong(it));
    }

    public void install(FormatterRegistry registry) {
        registry.addConverter(String.class, lrClass, this);
    }
}
