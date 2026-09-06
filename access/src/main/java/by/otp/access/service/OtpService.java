package by.otp.access.service;

import by.otp.access.config.properties.OtpProperties;
import by.otp.access.dto.otp.OtpChallenge;
import by.otp.access.security.otp.OtpGenerator;
import by.otp.access.validation.ContactValidator;
import by.otp.commonLib.enumeration.ContactType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpProperties otpProperties;

    private final ContactValidator contactValidator;
    private final OtpGenerator otpGenerator;
    private final OtpCacheService otpCacheService;

    public OtpChallenge create(ContactType contactType, String contact) {
        contactValidator.validate(contactType, contact);

        String code = otpGenerator.generate();
        otpCacheService.storeOtp(contact, code, otpProperties.ttl());

        return new OtpChallenge(contact, contactType, code);
    }

    public void invalidate(String contact) {
        otpCacheService.invalidate(contact);
    }
}
