package peaksoft.house.airbnbb9.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.exception.NotFoundException;
import peaksoft.house.airbnbb9.repository.UserRepository;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository userRepository;

    @Value("${spring.jwt.secret_key}")
    private String SECRET_KEY;

    public String generateToken(UserDetails userDetails) {
        return JWT.create()
                .withClaim("username", userDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(ZonedDateTime.now().plusDays(3).toInstant()))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public String validateToken(String token) {
        JWTVerifier jwtVerifier =
                JWT.require(
                                Algorithm.HMAC256(SECRET_KEY))
                        .build();

        DecodedJWT jwt = jwtVerifier.verify(token);
        return jwt.getClaim("username").asString();
    }

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.getUserByEmail(email).orElseThrow(() ->
                new NotFoundException(String.format("User with email: %s  not found!", email)));
    }
}