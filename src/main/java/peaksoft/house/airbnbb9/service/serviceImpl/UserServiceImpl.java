package peaksoft.house.airbnbb9.service.serviceImpl;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.config.JwtService;
import peaksoft.house.airbnbb9.dto.request.UserRequest;
import peaksoft.house.airbnbb9.dto.responce.UserResponse;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.service.UserService;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;



    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;
    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getFullName())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setFullName(userRequest.getFullName());
        user.setPassword(userRequest.getPassword());

        user = userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFullName(user.getUsername());
        userResponse.setToken(jwtService.generateToken(user));

        return userResponse;
    }
    public String getGoogleSignInUrl() {
        // Настраиваем параметры API Google Sign-In
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(),
                new JacksonFactory(),
                googleClientId,
                googleClientSecret,
                Collections.singletonList("email")
        ).setAccessType("offline").build();

        // Генерируем URL Google Sign-In

        return flow.newAuthorizationUrl()
                .setRedirectUri("http://localhost:8080/api/users/login/google/callback")
                .setResponseTypes(Collections.singletonList("code"))
                .build();
    }

    @Override
        public UserResponse processGoogleLogin(String code) {
            // Configure the Google Sign-In API settings
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    new NetHttpTransport(),
                    new JacksonFactory(),
                    googleClientId,
                    googleClientSecret,
                    Collections.singletonList("email")
            ).setAccessType("offline").build();

            // Exchange the authorization code for credentials
            GoogleTokenResponse tokenResponse;
            try {
                tokenResponse = flow.newTokenRequest(code)
                        .setRedirectUri("http://localhost:8080/api/users/login/google/callback")
                        .execute();
            } catch (IOException e) {
                throw new RuntimeException("Failed to retrieve token from Google");
            }

            // Retrieve the user information from Google
            GoogleIdToken idToken;
            try {
                GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                        .setAudience(Collections.singletonList(googleClientId))
                        .build();

                idToken = verifier.verify(tokenResponse.getIdToken());
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException("Failed to verify Google ID token");
            }

            if (idToken == null) {
                throw new IllegalArgumentException("Invalid Google ID token");
            }

            // Extract the user's email from the ID token
            String email = idToken.getPayload().getEmail();

            // Check if a user with the provided email already exists
            User user = userRepository.findByEmail(email);
            if (user == null) {
                // Create a new user entity
                user = new User();
                user.setEmail(email);

                // Save the user to the database
                user = userRepository.save(user);
            }

            // Create a user response with the user details
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setFullName(user.getUsername());
            userResponse.setToken(jwtService.generateToken(user));

            return userResponse;
    }

}
