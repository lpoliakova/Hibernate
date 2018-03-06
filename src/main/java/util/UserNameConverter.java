package util;

import user.UserName;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by oradchykova on 11/7/17.
 */
@Converter(autoApply = true)
public class UserNameConverter implements AttributeConverter<UserName, String> {

    @Override
    public String convertToDatabaseColumn(UserName name) {
        return name.toString();
    }

    @Override
    public UserName convertToEntityAttribute(String s) {
        return UserName.fromString(s);
    }
}
