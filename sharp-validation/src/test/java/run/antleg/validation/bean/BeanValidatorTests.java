package run.antleg.validation.bean;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeanValidatorTests {

    @Test
    void string() {
        var validator = new BeanValidator<>(Person.class)
                .stringProperty(Person::getName).isNotNull().isNotBlank().minLength(3)
                .next();
        var person = new Person();
        person.setName("张章章");
        var results = validator.validate(person);
        assertTrue(results.isEmpty());
    }

    // TODO: 完善

    static class Person {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
