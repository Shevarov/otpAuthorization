package by.otp.access.controller;

import by.otp.access.dto.contact.AddUserContactDto;
import by.otp.access.dto.contact.UpdateUserContactDto;
import by.otp.access.dto.contact.UserContactDto;
import by.otp.access.service.UserContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/contacts")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN') or authentication.principal.id == #userId")
public class UserContactController {

    private final UserContactService userContactService;

    @GetMapping
    public List<UserContactDto> getContacts(@PathVariable Long userId) {
        return userContactService.getContacts(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserContactDto addContact(@PathVariable Long userId, @RequestBody @Valid AddUserContactDto dto) {
        return userContactService.addContact(userId, dto);
    }

    @PatchMapping("/{contactId}")
    public UserContactDto updateContact(@PathVariable Long userId, @PathVariable Long contactId,
                                        @RequestBody @Valid UpdateUserContactDto dto) {
        return userContactService.updateContact(userId, contactId, dto);
    }

    @DeleteMapping("/{contactId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@PathVariable Long userId, @PathVariable Long contactId) {
        userContactService.deleteContact(userId, contactId);
    }
}
