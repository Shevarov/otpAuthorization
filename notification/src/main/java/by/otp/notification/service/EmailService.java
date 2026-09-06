package by.otp.notification.service;

/**
 * Sends an email to a recipient. Implementations decide the transport
 * (Gmail SMTP, another provider's API, etc.) — callers depend only on this interface.
 */
public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
