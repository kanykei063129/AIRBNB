package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.config.config.JwtService;
import peaksoft.house.airbnbb9.dto.AuthenticationResponse;
import peaksoft.house.airbnbb9.dto.SignInRequest;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.Role;
import peaksoft.house.airbnbb9.exceptoin.NotFoundException;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.service.UserService;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        User user = new User();
        user.setRole(Role.ADMIN);
        user.setFullName("Admin Admin kyzy");
        user.setEmail("admin@gmail.com");
        user.setImage("dfghjkl");
        user.setPassword(passwordEncoder.encode("admin123"));
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            userRepository.save(user);
        }
    }

    @Override
    public AuthenticationResponse authenticate(SignInRequest request) {
        User user = userRepository.getUserByEmail(request.email())
                .orElseThrow(() -> new NotFoundException(String.format
                        ("User with email: %s doesn't exists", request.email())));
        String token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .email(user.getEmail())
                .build();
    }
}
