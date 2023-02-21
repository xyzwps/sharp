package run.antleg.sharp.util;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public final class When<R> {

    private final Supplier<Boolean> condition;

    private final Supplier<R> whenTrueGet;


    private When(Supplier<Boolean> condition, Supplier<R> whenTrueGet) {
        this.condition = Objects.requireNonNull(condition);
        this.whenTrueGet = Objects.requireNonNull(whenTrueGet);
    }

    public Optional<R> get() {
        return Optional.ofNullable(condition.get() ? whenTrueGet.get() : null);
    }

    public R orElse(R result) {
        return condition.get() ? whenTrueGet.get() : result;
    }

    public R orElse(Supplier<R> rSupplier) {
        return condition.get() ? whenTrueGet.get() : rSupplier.get();
    }


    public static final class WhenBuilder {

        private final Supplier<Boolean> condition;

        private WhenBuilder(Supplier<Boolean> condition) {
            this.condition = condition;
        }

        public <R> When<R> then(Supplier<R> whenTrueGet) {
            return new When<>(this.condition, whenTrueGet);
        }
    }

    public static WhenBuilder when(Supplier<Boolean> condition) {
        return new WhenBuilder(condition);
    }

    public static WhenBuilder when(boolean condition) {
        return new WhenBuilder(() -> condition);
    }

}
