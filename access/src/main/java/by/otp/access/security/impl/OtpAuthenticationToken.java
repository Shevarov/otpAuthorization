package by.otp.access.security.impl;

import by.otp.commonLib.enumeration.ContactType;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Authentication token used for OTP-based sign-in over phone or email.
 */
public class OtpAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final Object credentials;
    @Getter
    private final ContactType contactType;

    private OtpAuthenticationToken(Object principal, Object credentials, ContactType contactType) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.contactType = contactType;
        setAuthenticated(false);
    }

    private OtpAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = null;
        this.contactType = null;
        super.setAuthenticated(true);
    }

    public static OtpAuthenticationToken unauthenticated(Object principal, Object credentials, ContactType contactType) {
        return new OtpAuthenticationToken(principal, credentials, contactType);
    }

    public static OtpAuthenticationToken authenticated(Object principal, Collection<? extends GrantedAuthority> authorities) {
        return new OtpAuthenticationToken(principal, authorities);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
