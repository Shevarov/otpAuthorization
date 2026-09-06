package by.otp.access.repository;

import by.otp.access.model.UserContact;
import by.otp.commonLib.enumeration.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserContactRepository extends JpaRepository<UserContact, Long> {
    boolean existsByTypeAndValue(ContactType type, String value);
}
