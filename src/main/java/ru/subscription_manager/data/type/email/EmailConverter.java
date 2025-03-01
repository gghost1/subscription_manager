package ru.subscription_manager.data.type.email;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.regex.Pattern;

@Converter
public class EmailConverter implements AttributeConverter<String, String> {

    // Регулярное выражение из PostgreSQL-домена
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;

        // Проверка формата перед сохранением в БД
        if (!EMAIL_PATTERN.matcher(attribute).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return attribute;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}