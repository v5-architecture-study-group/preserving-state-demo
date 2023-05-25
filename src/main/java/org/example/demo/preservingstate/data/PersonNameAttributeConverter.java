package org.example.demo.preservingstate.data;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PersonNameAttributeConverter implements AttributeConverter<PersonName, String> {

    @Override
    public String convertToDatabaseColumn(PersonName attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public PersonName convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new PersonName(dbData);
    }
}
