package by.otp.access.validation;

import by.otp.commonLib.enumeration.ContactType;

public interface ContactValidation {
    boolean validate(String value);
    ContactType getContactType();
}
