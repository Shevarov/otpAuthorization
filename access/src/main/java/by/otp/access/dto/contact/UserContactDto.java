package by.otp.access.dto.contact;

import by.otp.commonLib.enumeration.ContactType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserContactDto {
    private Long id;
    private ContactType contactType;
    private String contactValue;
    private boolean primary;
}
