package run.antleg.validation.bean;

public sealed interface RuleResult {

    static OK ok() {
        return OK.INSTANCE;
    }

    static Invalid invalid(String message) {
        return new Invalid(message);
    }

    record OK() implements RuleResult {
        static OK INSTANCE = new OK();
    }

    record Invalid(String message) implements RuleResult {
    }
}
