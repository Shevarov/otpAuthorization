package by.otp.access.service;

import by.otp.access.dto.auth.OtpRequestDto;
import by.otp.access.dto.otp.OtpChallenge;
import by.otp.access.exception.NotificationDeliveryException;
import by.otp.access.exception.OtpDeliveryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpRequestService {

    private final OtpService otpService;
    private final OtpNotificationService otpNotificationService;

    public void request(OtpRequestDto request) {
        OtpChallenge challenge = otpService.create(request.getContactType(), request.getContact());

        try {
            otpNotificationService.send(challenge);
        } catch (NotificationDeliveryException e) {
            otpService.invalidate(challenge.contact());
            throw new OtpDeliveryException("Failed to deliver OTP code to " + challenge.contact(), e);
        }
    }
}
