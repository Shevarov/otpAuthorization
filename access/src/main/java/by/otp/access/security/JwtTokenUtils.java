package by.otp.access.security;

import by.otp.access.config.properties.SecurityProperties;
import by.otp.access.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import io.jsonwebtoken.security.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    private static final String CLAIM_AUTHORITIES = "authorities";
    private static final String CLAIM_TOKEN_TYPE = "token_type";
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = Jwts.SIG.RS256;
    private final KeyPair keyPair = SIGNATURE_ALGORITHM.keyPair().build();

    private final SecurityProperties securityProperties;

    public PublicJwk<PublicKey> getJwk() {
        return Jwks.builder().key(keyPair.getPublic()).build();
    }

    public String generateAccessToken(User subject) {
        String authorities = subject.getRoleList().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(String.valueOf(subject.getId()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + securityProperties.accessTokenLifetime().toMillis()))
                .claim(CLAIM_AUTHORITIES, authorities)
                .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_ACCESS)
                .signWith(keyPair.getPrivate(), SIGNATURE_ALGORITHM)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + securityProperties.refreshTokenLifetime().toMillis()))
                .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_REFRESH)
                .signWith(keyPair.getPrivate(), SIGNATURE_ALGORITHM)
                .compact();
    }

    public boolean isAccessToken(String token) {
        return TOKEN_TYPE_ACCESS.equals(extractClaim(token, claims -> claims.get(CLAIM_TOKEN_TYPE, String.class)));
    }

    public boolean isRefreshToken(String token) {
        return TOKEN_TYPE_REFRESH.equals(extractClaim(token, claims -> claims.get(CLAIM_TOKEN_TYPE, String.class)));
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        String authorities = extractClaim(token, claims -> claims.get(CLAIM_AUTHORITIES, String.class));
        if (!StringUtils.hasText(authorities)) {
            return List.of();
        }
        return Arrays.stream(authorities.split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(keyPair.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}
