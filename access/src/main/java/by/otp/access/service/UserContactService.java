package by.otp.access.service;

import by.otp.access.dto.contact.AddUserContactDto;
import by.otp.access.dto.contact.UpdateUserContactDto;
import by.otp.access.dto.contact.UserContactDto;

import java.util.List;

public interface UserContactService {
    List<UserContactDto> getContacts(Long userId);

    UserContactDto addContact(Long userId, AddUserContactDto dto);

    UserContactDto updateContact(Long userId, Long contactId, UpdateUserContactDto dto);

    void deleteContact(Long userId, Long contactId);
}
