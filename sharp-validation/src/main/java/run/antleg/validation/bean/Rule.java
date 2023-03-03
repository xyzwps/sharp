package run.antleg.validation.bean;

import java.util.function.Function;

public interface Rule<V, B> {
    V getValue(B bean);

    RuleResult validate(V v);

    static <V, B> Rule<V, B> create(
            Function<B, V> getValueFn,
            Function<V, RuleResult> validateFn
    ) {
        return new Rule<>() {
            @Override
            public V getValue(B bean) {
                return getValueFn.apply(bean);
            }

            @Override
            public RuleResult validate(V v) {
                return validateFn.apply(v);
            }
        };
    }
}
