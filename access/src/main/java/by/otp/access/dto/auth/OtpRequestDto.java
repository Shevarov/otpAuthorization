package by.otp.access.dto.auth;

import by.otp.commonLib.enumeration.ContactType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequestDto {
    @NotBlank(message = "Request body parameter 'contact' must not be blank")
    private String contact;
    @NotNull(message = "Request body parameter 'contactType' must not be null")
    private ContactType contactType;
}
