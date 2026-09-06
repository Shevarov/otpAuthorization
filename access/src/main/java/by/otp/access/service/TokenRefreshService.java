package by.otp.access.service;

import by.otp.access.dto.auth.AuthResponseDto;
import by.otp.access.model.User;
import by.otp.access.repository.UserRepository;
import by.otp.access.security.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenRefreshService {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;

    public AuthResponseDto refresh(String refreshToken) {

        String userId = extractRefreshSubjectId(refreshToken);

        User user = userRepository.findUserById(Long.valueOf(userId))
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token subject " + userId));

        return AuthResponseDto.builder()
                .accessToken(jwtTokenUtils.generateAccessToken(user))
                .refreshToken(jwtTokenUtils.generateRefreshToken(user.getId()))
                .build();
    }

    private String extractRefreshSubjectId(String refreshToken) {
        try {
            if (!jwtTokenUtils.isRefreshToken(refreshToken)) {
                throw new BadCredentialsException("Not a refresh token");
            }
            return jwtTokenUtils.extractUserId(refreshToken);
        } catch (SignatureException e) {
            throw new BadCredentialsException("Token has invalid signature");
        } catch (ExpiredJwtException e) {
            throw new BadCredentialsException("Token is expired");
        }
    }
}
