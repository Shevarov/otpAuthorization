package by.otp.access.dto.otp;

import by.otp.commonLib.enumeration.ContactType;

public record OtpChallenge(
        String contact,
        ContactType contactType,
        String code
) {
}
