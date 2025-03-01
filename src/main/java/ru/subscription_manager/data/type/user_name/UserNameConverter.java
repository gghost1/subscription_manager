package ru.subscription_manager.data.type.user_name;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UserNameConverter implements AttributeConverter<UserName, String> {

    @Override
    public String convertToDatabaseColumn(UserName userName) {
        if (userName == null) {
            return null;
        }
            String value = String.format("(%s,%s,%s)",
                    userName.getFirstName(),
                    userName.getSecondName(),
                    userName.getLastName()
            );
            return value;
    }

    @Override
    public UserName convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        dbData = dbData.substring(1, dbData.length() - 1);
        String[] parts = dbData.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid value for UserName: " + dbData);
        }
        return new UserName(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim().equals("null") ? null : parts[2].trim()
        );
    }
}