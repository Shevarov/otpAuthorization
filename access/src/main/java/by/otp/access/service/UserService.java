package by.otp.access.service;

import by.otp.access.dto.role.AssignRolesDto;
import by.otp.access.dto.user.CreateUserDto;
import by.otp.access.dto.user.UpdateUserDto;
import by.otp.access.dto.user.UserDto;
import by.otp.access.model.User;
import by.otp.commonLib.enumeration.ContactType;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    UserDto createUser(CreateUserDto dto);

    UserDto updateUser(Long id, UpdateUserDto dto);

    UserDto assignRoles(Long id, AssignRolesDto dto);

    Optional<User> findByContact(ContactType type, String value);

    User findOrRegisterByContact(ContactType type, String value);
}
