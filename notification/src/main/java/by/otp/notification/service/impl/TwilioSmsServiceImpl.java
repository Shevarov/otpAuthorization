package by.otp.notification.service.impl;

import by.otp.notification.service.SmsService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwilioSmsServiceImpl implements SmsService {

    @Value("${twilio.phone-number}")
    private String from;

    @Override
    public String sendSms(String to, String message) {
        Message sms = Message
                .creator(new PhoneNumber(to),new PhoneNumber(from),message)
                .create();

        return sms.getSid();
    }
}
