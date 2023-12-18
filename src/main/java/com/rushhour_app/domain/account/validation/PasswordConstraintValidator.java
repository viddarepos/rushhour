package com.rushhour_app.domain.account.validation;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Properties props = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("passay.properties");

        try {
            props.load(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        MessageResolver resolver = new PropertiesMessageResolver((props));
        PasswordValidator validator = new PasswordValidator(resolver, Arrays.asList(
                new LengthRule(8, 25),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()

        ));

        RuleResult result = validator.validate(new PasswordData(s));
        if (result.isValid()) {
            return true;
        }

        List<String> messages = validator.getMessages(result);
        String messageTemplate = String.join(" ", messages);
        constraintValidatorContext.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation().disableDefaultConstraintViolation();

        return false;
    }
}
