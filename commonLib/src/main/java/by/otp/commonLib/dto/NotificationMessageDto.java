package by.otp.commonLib.dto;

import by.otp.commonLib.enumeration.ContactType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * A notification message travelling through Kafka from the access service
 * to the notification service. Depending on {@link #contactType} it is
 * delivered either as an SMS or as an email.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessageDto {
    private String contact;
    private String message;
    private ContactType contactType;
    private Map<String, Object> metadata;
}
