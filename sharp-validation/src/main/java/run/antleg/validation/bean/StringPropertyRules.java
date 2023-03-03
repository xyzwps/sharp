package run.antleg.validation.bean;

import java.util.Objects;
import java.util.function.Function;

public class StringPropertyRules<B> implements Rules {

    private final BeanValidator<B> b;

    private final Function<B, String> getValueFn;

    public StringPropertyRules(BeanValidator<B> b, Function<B, String> getValueFn) {
        this.b = Objects.requireNonNull(b);
        this.getValueFn = Objects.requireNonNull(getValueFn);
    }

    private StringPropertyRules<B> addRule(Function<String, RuleResult> validFn) {
        b.addRule(Rule.create(getValueFn, validFn));
        return this;
    }

    public StringPropertyRules<B> isNotNull() {
        return addRule(s -> s == null ? RuleResult.invalid("not null") : RuleResult.ok());
    }

    public StringPropertyRules<B> isNotEmpty() {
        return addRule(s -> s == null || s.isEmpty() ? RuleResult.invalid("not empty") : RuleResult.ok());
    }

    public StringPropertyRules<B> isNotBlank() {
        return addRule(s -> s == null || s.isEmpty() ? RuleResult.invalid("not blank") : RuleResult.ok());
    }

    public StringPropertyRules<B> minLength(int minLen) {
        return addRule(s -> s.length() < minLen ? RuleResult.invalid("length is less than " + minLen) : RuleResult.ok());
    }

    public StringPropertyRules<B> maxLength(int maxLen) {
        return addRule(s -> s.length() > maxLen ? RuleResult.invalid("length is greater than " + maxLen) : RuleResult.ok());
    }

    public BeanValidator<B> next() {
        return b;
    }
}
