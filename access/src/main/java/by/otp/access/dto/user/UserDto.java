package by.otp.access.dto.user;

import by.otp.access.dto.contact.UserContactDto;
import by.otp.access.dto.role.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private Set<RoleDto> roles;
    private List<UserContactDto> contacts;
}
