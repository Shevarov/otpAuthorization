package by.otp.access.validation;

import by.otp.access.dto.contact.ContactInputDto;
import by.otp.commonLib.enumeration.ContactType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ContactValidator {
    private final Map<ContactType, ContactValidation> contactByContactType;

    public ContactValidator(List<ContactValidation> contactValidationList) {
        this.contactByContactType = contactValidationList.stream()
                .collect(Collectors.toMap(ContactValidation::getContactType, Function.identity()));
    }

    public void validate(ContactInputDto contactInputDto) {
        validate(contactInputDto.getContactType(), contactInputDto.getValue());
    }

    public void validate(ContactType type, String contactValue) {
        ContactValidation contactValidation = contactByContactType.get(type);
        if (contactValidation == null) {
            throw new IllegalArgumentException("Unsupported contact type " + type);
        }
        if (!contactValidation.validate(contactValue)) {
            throw new IllegalArgumentException("Invalid contact value %s".formatted(contactValue));
        }
    }
}
