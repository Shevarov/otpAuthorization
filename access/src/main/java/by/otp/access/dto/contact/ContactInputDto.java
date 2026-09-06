package by.otp.access.dto.contact;

import by.otp.commonLib.enumeration.ContactType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInputDto {
    @NotNull(message = "Request body parameter 'contactType' must not be null")
    private ContactType contactType;
    @NotBlank(message = "Request body parameter 'value' must not be blank")
    private String value;
}
