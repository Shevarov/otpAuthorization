package by.otp.access.service.impl;

import by.otp.access.dto.auth.AuthResponseDto;
import by.otp.access.dto.auth.OtpRequestDto;
import by.otp.access.dto.auth.VerifyOtpDto;
import by.otp.access.security.JwtTokenUtils;
import by.otp.access.service.AuthService;
import by.otp.access.service.LoginService;
import by.otp.access.service.OtpRequestService;
import by.otp.access.service.TokenRefreshService;
import io.jsonwebtoken.security.PublicJwk;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.PublicKey;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenUtils jwtTokenUtils;

    private final LoginService loginService;
    private final OtpRequestService otpRequestService;
    private final TokenRefreshService tokenRefreshService;

    @Override
    public PublicJwk<PublicKey> jwk() {
        return jwtTokenUtils.getJwk();
    }

    @Override
    public AuthResponseDto login(VerifyOtpDto body) {
        return loginService.login(body);
    }

    @Override
    public AuthResponseDto refresh(String refreshToken) {
        return tokenRefreshService.refresh(refreshToken);
    }

    @Override
    public void requestOtp(OtpRequestDto body) {
        otpRequestService.request(body);
    }
}
