package by.otp.access.service;

import by.otp.access.dto.auth.AuthResponseDto;
import by.otp.access.dto.auth.VerifyOtpDto;
import by.otp.access.model.User;
import by.otp.access.security.JwtTokenUtils;
import by.otp.access.security.impl.OtpAuthenticationToken;
import by.otp.access.validation.ContactValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final ContactValidator contactValidator;

    public AuthResponseDto login(VerifyOtpDto body) {
        contactValidator.validate(body.getContactType(), body.getContact());

        Authentication authentication = authenticationManager.authenticate(
                OtpAuthenticationToken.unauthenticated(body.getContact(), body.getCode(), body.getContactType()));

        User user = (User) authentication.getPrincipal();

        return AuthResponseDto.builder()
                .accessToken(jwtTokenUtils.generateAccessToken(user))
                .refreshToken(jwtTokenUtils.generateRefreshToken(user.getId()))
                .build();
    }
}
