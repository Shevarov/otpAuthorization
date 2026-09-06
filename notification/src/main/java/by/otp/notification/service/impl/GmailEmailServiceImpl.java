package by.otp.notification.service.impl;

import by.otp.notification.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Sends email through a Gmail SMTP account (smtp.gmail.com:587, STARTTLS)
 * using Spring's {@link JavaMailSender}, which is auto-configured from the
 * {@code spring.mail.*} properties (host/port/username/app-password).
 */
@Service
@RequiredArgsConstructor
public class GmailEmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);

            mailSender.send(message);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to send email to " + to, e);
        }
    }
}
