package run.antleg.sharp.config.hibernate;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.AttributeConverter;
import run.antleg.sharp.util.JSON;

import java.util.Set;

public class LongSetAttributeConverter implements AttributeConverter<Set<Long>, String> {
    @Override
    public String convertToDatabaseColumn(Set<Long> attribute) {
        return JSON.stringify(attribute);
    }

    @Override
    public Set<Long> convertToEntityAttribute(String dbData) {
        return JSON.parse(dbData, longSetType);
    }

    private static final TypeReference<Set<Long>> longSetType = new TypeReference<>() {
    };

}
