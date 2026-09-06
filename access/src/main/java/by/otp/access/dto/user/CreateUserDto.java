package by.otp.access.dto.user;

import by.otp.access.dto.contact.ContactInputDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
    private String name;
    @NotNull(message = "Request body parameter 'userContact' must not be null")
    @Valid
    private ContactInputDto userContact;
    private Set<String> roleNames;
}
