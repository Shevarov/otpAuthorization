package by.otp.access.security.filter;

import by.otp.access.model.User;
import by.otp.access.repository.UserRepository;
import by.otp.access.security.JwtTokenUtils;
import by.otp.access.security.entrypoint.SecurityErrorResponseWriter;
import by.otp.access.security.impl.OtpAuthenticationToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final SecurityErrorResponseWriter responseWriter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(BEARER_PREFIX.length());
        String userId;
        try {
            if (!jwtTokenUtils.isAccessToken(jwt)) {
                throw new SignatureException("Token type is not valid");
            }
            userId = jwtTokenUtils.extractUserId(jwt);
        } catch (SignatureException e) {
            responseWriter.write(response, HttpStatus.UNAUTHORIZED, "Token has invalid signature");
            return;
        } catch (ExpiredJwtException e) {
            responseWriter.write(response, HttpStatus.UNAUTHORIZED, "Token is expired");
            return;
        }

        Authentication existingAuthentication = SecurityContextHolder.getContext().getAuthentication();

        if (StringUtils.hasText(userId) && existingAuthentication == null) {
            Optional<User> optionalUser = userRepository.findUserById(Long.valueOf(userId));
            if (optionalUser.isEmpty()) {
                responseWriter.write(response, HttpStatus.UNAUTHORIZED, "Token has invalid subject");
                return;
            }

            List<SimpleGrantedAuthority> authorities = jwtTokenUtils.extractAuthorities(jwt);
            OtpAuthenticationToken authToken = OtpAuthenticationToken.authenticated(optionalUser.get(), authorities);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
