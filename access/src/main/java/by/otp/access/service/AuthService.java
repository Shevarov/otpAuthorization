package by.otp.access.service;

import by.otp.access.dto.auth.AuthResponseDto;
import by.otp.access.dto.auth.OtpRequestDto;
import by.otp.access.dto.auth.VerifyOtpDto;
import io.jsonwebtoken.security.PublicJwk;

import java.security.PublicKey;

public interface AuthService {

    AuthResponseDto login(VerifyOtpDto verifyOtpDto);

    AuthResponseDto refresh(String refreshToken);

    PublicJwk<PublicKey> jwk();

    void requestOtp(OtpRequestDto body);

}
