package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.config.JwtService;
import peaksoft.house.airbnbb9.dto.request.SignInRequest;
import peaksoft.house.airbnbb9.dto.responce.AuthenticationResponse;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.exceptoin.BadCredentialException;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.service.AuthenticationService;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponse signIn(SignInRequest signInRequest) {
        if (signInRequest.getEmail().isBlank()) {
            throw new BadCredentialsException("Email doesn't exist!");
        }
        User user = userRepository.getUserByEmail(signInRequest.getEmail())
                .orElseThrow(() -> {
                    log.error(String.format("User with email : %s not found !", signInRequest.getEmail()));
                    return new EntityExistsException(String.format("User with email : %s not found !", signInRequest.getEmail()));
                });
        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            log.error("Incorrect password !");
            throw new BadCredentialException("Incorrect password !");
        }
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .token(jwtToken)
                .build();
    }
}

