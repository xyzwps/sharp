package run.antleg.validation.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class BeanValidator<B> {

    private final Class<B> b;
    private final List<Rule<?, B>> allRules;

    public BeanValidator(Class<B> b) {
        this.b = Objects.requireNonNull(b);
        this.allRules = new ArrayList<>();
    }

    public StringPropertyRules<B> stringProperty(Function<B, String> getValue) {
        return new StringPropertyRules<>(this, getValue);
    }

    public List<RuleResult.Invalid> validate(B bean) {
        var results = new ArrayList<RuleResult.Invalid>();
        for (var rule : this.allRules) {
            @SuppressWarnings("unchecked")
            var theRule = (Rule<Object, B>) rule;
            var value = theRule.getValue(bean);
            var result = theRule.validate(value);
            if (result instanceof RuleResult.Invalid i) {
                results.add(i);
            }
        }
        return results;
    }

    BeanValidator<B> addRule(Rule<?, B> rule) {
        this.allRules.add(rule);
        return this;
    }
}
