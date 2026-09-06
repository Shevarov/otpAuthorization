package by.otp.access.controller;

import by.otp.access.dto.role.AssignRolesDto;
import by.otp.access.dto.user.CreateUserDto;
import by.otp.access.dto.user.UpdateUserDto;
import by.otp.access.dto.user.UserDto;
import by.otp.access.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN') or authentication.principal.id == #id")
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Validated CreateUserDto user) {
        return userService.createUser(user);
    }

    @PreAuthorize("hasAuthority('ADMIN') or authentication.principal.id == #id")
    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserDto user) {
        return userService.updateUser(id, user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/roles")
    public UserDto assignRoles(@PathVariable Long id, @RequestBody @Valid AssignRolesDto dto) {
        return userService.assignRoles(id, dto);
    }
}
