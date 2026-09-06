package by.otp.access.security.impl;

import by.otp.access.model.User;
import by.otp.access.service.OtpCacheService;
import by.otp.access.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Authenticates a user by verifying a one-time password sent to their contact (phone or email).
 * On the first successful verification a user is transparently registered.
 */
@Component
@RequiredArgsConstructor
public class OtpAuthenticationProvider implements AuthenticationProvider {
    private final OtpCacheService otpCacheService;
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OtpAuthenticationToken token = (OtpAuthenticationToken) authentication;
        String contact = (String) token.getPrincipal();
        String code = (String) token.getCredentials();

        if (!otpCacheService.verifyOtpAndRemove(contact, code)) {
            throw new BadCredentialsException("Invalid OTP code");
        }

        User user;
        try {
            user = userService.findOrRegisterByContact(token.getContactType(), contact);
        } catch (RuntimeException ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }

        return OtpAuthenticationToken.authenticated(user, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
