package by.otp.notification.processor;

import by.otp.commonLib.enumeration.ContactType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationProcessorFactory {

    private final List<NotificationProcessor> processors;

    private final Map<ContactType, NotificationProcessor> processorByContactType = new HashMap<>();

    @PostConstruct
    public void initProcessorCache() {
        for (NotificationProcessor processor : processors) {
            processorByContactType.putIfAbsent(processor.getContactType(), processor);
        }
    }

    public NotificationProcessor getProcessor(ContactType contactType) {
        return processorByContactType.getOrDefault(contactType, processorByContactType.get(null));
    }
}
