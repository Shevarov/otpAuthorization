package by.otp.access.model;

import by.otp.commonLib.enumeration.ContactType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "users_contact",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"type", "value"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserContact {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_contact_sequence")
    @SequenceGenerator(name = "user_contact_sequence", sequenceName = "user_contact_sequence", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private ContactType type;

    @Column(length = 128)
    private String value; // +7999..., test@mail.com, telegramId

    @Column(name = "is_primary", nullable = false)
    private boolean primary;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer()
                .getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserContact userContact = (UserContact) o;
        return getId() != null && Objects.equals(getId(), userContact.getId());

    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass()
                .hashCode()
                : getClass().hashCode();
    }
}
