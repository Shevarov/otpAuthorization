package by.otp.access.controller;

import by.otp.access.dto.auth.AuthResponseDto;
import by.otp.access.dto.auth.OtpRequestDto;
import by.otp.access.dto.auth.RefreshTokenRequestDto;
import by.otp.access.dto.auth.VerifyOtpDto;
import by.otp.access.service.AuthService;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/jwk")
    public PublicJwk<PublicKey> jwk() {
        return authService.jwk();
    }

    @PostMapping("/otp")
    public Map<String, String> requestOtp(@RequestBody @Valid OtpRequestDto body) {
        authService.requestOtp(body);
        return Map.of("status", "OTP_SENT");
    }

    @PostMapping("/token")
    public AuthResponseDto login(@RequestBody @Valid VerifyOtpDto body) {
        return authService.login(body);
    }

    @PostMapping("/refresh")
    public AuthResponseDto refresh(@RequestBody @Valid RefreshTokenRequestDto body) {
        return authService.refresh(body.getRefreshToken());
    }
}
