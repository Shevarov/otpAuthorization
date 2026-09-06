package by.otp.access.dto.role;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignRolesDto {
    @NotEmpty(message = "Request body parameter 'roleNames' must not be empty")
    private Set<String> roleNames;
}
