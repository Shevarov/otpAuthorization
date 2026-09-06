package by.otp.access.service.impl;

import by.otp.access.dto.contact.AddUserContactDto;
import by.otp.access.dto.contact.UpdateUserContactDto;
import by.otp.access.dto.contact.UserContactDto;
import by.otp.access.exception.DuplicateContactException;
import by.otp.access.exception.RecordNotFoundException;
import by.otp.access.exception.LastContactRemainingException;
import by.otp.access.mapper.UserContactMapper;
import by.otp.access.model.User;
import by.otp.access.model.UserContact;
import by.otp.access.repository.UserContactRepository;
import by.otp.access.repository.UserRepository;
import by.otp.access.service.UserContactService;
import by.otp.access.validation.ContactValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserContactServiceImpl implements UserContactService {

    private final UserRepository userRepository;
    private final UserContactRepository userContactRepository;
    private final ContactValidator contactValidator;
    private final UserContactMapper contactMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserContactDto> getContacts(Long userId) {
        return getUserOrNotFound(userId).getContactList().stream()
                .map(contactMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public UserContactDto addContact(Long userId, AddUserContactDto dto) {
        User user = getUserOrNotFound(userId);
        contactValidator.validate(dto.getContactType(), dto.getValue());

        if (userContactRepository.existsByTypeAndValue(dto.getContactType(), dto.getValue())) {
            throw new DuplicateContactException("Contact already exists: " + dto.getContactType() + ":" + dto.getValue());
        }

        boolean makePrimary = dto.isPrimary() || user.getContactList().isEmpty();
        if (makePrimary) {
            demoteCurrentPrimary(user);
        }

        UserContact contact = UserContact.builder()
                .type(dto.getContactType())
                .value(dto.getValue())
                .primary(makePrimary)
                .user(user)
                .build();

        user.getContactList().add(contact);
        userRepository.save(user);

        return contactMapper.toDto(contact);
    }

    @Override
    @Transactional
    public UserContactDto updateContact(Long userId, Long contactId, UpdateUserContactDto dto) {
        User user = getUserOrNotFound(userId);
        UserContact contact = getOwnedContactOrNotFound(user, contactId);

        if (dto.getValue() != null && !dto.getValue().equals(contact.getValue())) {
            contactValidator.validate(contact.getType(), dto.getValue());
            if (userContactRepository.existsByTypeAndValue(contact.getType(), dto.getValue())) {
                throw new DuplicateContactException(
                        "Contact already exists: " + contact.getType() + ":" + dto.getValue());
            }
            contact.setValue(dto.getValue());
        }

        if (Boolean.TRUE.equals(dto.getPrimary()) && !contact.isPrimary()) {
            demoteCurrentPrimary(user);
            contact.setPrimary(true);
        }

        userRepository.save(user);
        return contactMapper.toDto(contact);
    }

    @Override
    @Transactional
    public void deleteContact(Long userId, Long contactId) {
        User user = getUserOrNotFound(userId);
        UserContact contact = getOwnedContactOrNotFound(user, contactId);

        if (user.getContactList().size() == 1) {
            throw new LastContactRemainingException("User must have at least one contact method");
        }

        boolean wasPrimary = contact.isPrimary();
        user.getContactList().remove(contact);

        if (wasPrimary) {
            user.getContactList().stream()
                    .min(Comparator.comparing(UserContact::getId))
                    .ifPresent(next -> next.setPrimary(true));
        }

        userRepository.save(user);
    }

    private void demoteCurrentPrimary(User user) {
        user.getContactList().forEach(c -> c.setPrimary(false));
    }

    private UserContact getOwnedContactOrNotFound(User user, Long contactId) {
        return user.getContactList().stream()
                .filter(c -> c.getId().equals(contactId))
                .findFirst()
                .orElseThrow(() -> new RecordNotFoundException(
                        "Contact %d not found for user %d".formatted(contactId, user.getId())));
    }

    private User getUserOrNotFound(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("User with id %d was not found".formatted(userId)));
    }
}
