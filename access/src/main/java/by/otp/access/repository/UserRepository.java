package by.otp.access.repository;

import by.otp.access.model.User;
import by.otp.commonLib.enumeration.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
                select u
                from User u
                join fetch u.contactList c
                where c.type = :type
                  and c.value = :value
            """)
    Optional<User> findByContact(@Param("type") ContactType type, @Param("value") String value);

    Optional<User> findUserById(Long id);
}
