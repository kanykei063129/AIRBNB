package peaksoft.house.airbnbb9.service.serviceImpl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.config.security.JwtService;
import peaksoft.house.airbnbb9.dto.request.SignInRequest;
import peaksoft.house.airbnbb9.dto.response.AuthenticationResponse;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.Role;
import peaksoft.house.airbnbb9.exceptoin.BadCredentialException;
import peaksoft.house.airbnbb9.exceptoin.NotFoundException;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.service.AuthenticationService;

import java.io.IOException;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostConstruct
    public void init() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                new ClassPathResource("package.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials).build();
        FirebaseApp.initializeApp(firebaseOptions);
    }

    @Override
    public AuthenticationResponse signInWithGoogle(String tokenId) throws FirebaseAuthException {
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(tokenId);
        User user;
        if (!userRepository.existsByEmail(firebaseToken.getEmail())) {
            user = new User();
            user.setFullName(firebaseToken.getName());
            user.setEmail(firebaseToken.getEmail());
            user.setPassword(firebaseToken.getEmail());
            user.setRole(Role.USER);
            userRepository.save(user);
        }
        user = userRepository.getUserByEmail(firebaseToken.getEmail()).orElseThrow(
                () -> {
                    log.error("User with this email not found!");
                    return new NotFoundException("User with this email not found!");
                }
        );
        String token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest signInRequest) {
        if (signInRequest.getEmail().isBlank()) {
            throw new BadCredentialsException("Email doesn't exist!");
        }
        User user = userRepository.getUserByEmail(signInRequest.getEmail())
                .orElseThrow(() -> {
                    log.error(String.format("User with email : %s not found !", signInRequest.getEmail()));
                    return new NotFoundException(String.format("User with email : %s not found !", signInRequest.getEmail()));
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

