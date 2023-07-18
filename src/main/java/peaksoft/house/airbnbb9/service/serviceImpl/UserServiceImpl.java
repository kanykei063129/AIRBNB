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

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public User saveUser(String fullName, String email) {
        // Проверьте, существует ли пользователь с таким email в базе данных
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            return existingUser;
        }

        // Если пользователь не существует, создайте новую запись пользователя и сохраните в базу данных
        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        return userRepository.save(newUser);
    }

}




