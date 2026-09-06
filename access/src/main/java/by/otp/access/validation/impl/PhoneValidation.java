package by.otp.access.validation.impl;

import by.otp.access.config.properties.ValidationProperties;
import by.otp.access.validation.ContactValidation;
import by.otp.commonLib.enumeration.ContactType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhoneValidation implements ContactValidation {
    private final ValidationProperties validationProperties;

    @Override
    public boolean validate(String value) {
        return value != null && value.matches(validationProperties.phonePattern());
    }

    @Override
    public ContactType getContactType() {
        return ContactType.PHONE;
    }
}
