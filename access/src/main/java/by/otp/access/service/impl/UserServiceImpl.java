package by.otp.access.service.impl;

import by.otp.access.dto.contact.ContactInputDto;
import by.otp.access.dto.role.AssignRolesDto;
import by.otp.access.dto.user.CreateUserDto;
import by.otp.access.dto.user.UpdateUserDto;
import by.otp.access.dto.user.UserDto;
import by.otp.access.exception.DuplicateContactException;
import by.otp.access.exception.RecordNotFoundException;
import by.otp.access.mapper.UserMapper;
import by.otp.access.model.Role;
import by.otp.access.model.User;
import by.otp.access.model.UserContact;
import by.otp.access.repository.UserContactRepository;
import by.otp.access.repository.UserRepository;
import by.otp.access.service.RoleService;
import by.otp.access.service.UserService;
import by.otp.access.validation.ContactValidator;
import by.otp.commonLib.enumeration.ContactType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserContactRepository userContactRepository;
    private final ContactValidator contactValidator;
    private final UserMapper userMapper;
    private final RoleService roleService;

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        return userMapper.userToUserDto(getUserByIdOrNotFound(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDto)
                .toList();
    }

    @Override
    @Transactional
    public UserDto createUser(CreateUserDto dto) {
        ContactInputDto contactInput = dto.getUserContact();
        contactValidator.validate(contactInput);

        if (userContactRepository.existsByTypeAndValue(contactInput.getContactType(), contactInput.getValue())) {
            throw duplicateContact(contactInput.getContactType(), contactInput.getValue());
        }

        Set<Role> roles = roleService.resolveRoles(dto.getRoleNames());

        User user = User.builder()
                .name(dto.getName())
                .roleList(roles)
                .createdAt(LocalDateTime.now())
                .build();

        // Единственный контакт при создании — всегда primary.
        UserContact contact = UserContact.builder()
                .type(contactInput.getContactType())
                .value(contactInput.getValue())
                .primary(true)
                .user(user)
                .build();
        user.setContactList(new ArrayList<>(List.of(contact)));

        return userMapper.userToUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UpdateUserDto dto) {
        User user = getUserByIdOrNotFound(id);
        user.setName(dto.getName());
        return userMapper.userToUserDto(userRepository.save(user));
    }

    @Override
    public Optional<User> findByContact(ContactType type, String value) {
        return userRepository.findByContact(type, value);
    }

    @Override
    @Transactional
    public UserDto assignRoles(Long id, AssignRolesDto dto) {
        User user = getUserByIdOrNotFound(id);
        user.setRoleList(roleService.resolveRoles(dto.getRoleNames()));
        return userMapper.userToUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public User findOrRegisterByContact(ContactType type, String value) {
        return findByContact(type, value).orElseGet(() -> registerByContact(type, value));
    }

    private User registerByContact(ContactType type, String value) {
        if (userContactRepository.existsByTypeAndValue(type, value)) {
            return findByContact(type, value).orElseThrow(() -> duplicateContact(type, value));
        }
        return userRepository.save(newUserWithContact(null, type, value));
    }

    private User newUserWithContact(String name, ContactType type, String value) {
        User user = User.builder()
                .name(name)
                .roleList(Set.of(roleService.getDefaultRole()))
                .createdAt(LocalDateTime.now())
                .build();

        UserContact contact = UserContact.builder()
                .type(type)
                .value(value)
                .primary(true)
                .user(user)
                .build();

        user.setContactList(List.of(contact));
        return user;
    }

    private User getUserByIdOrNotFound(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User with id %d was not found", id)));
    }

    private DuplicateContactException duplicateContact(ContactType type, String value) {
        return new DuplicateContactException("User already exists for contact " + type + ":" + value);
    }
}
