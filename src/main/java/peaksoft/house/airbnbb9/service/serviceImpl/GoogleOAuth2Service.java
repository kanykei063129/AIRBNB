package peaksoft.house.airbnbb9.service.serviceImpl;

import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.service.UserService;

@Service
public class GoogleOAuth2Service {
    private final UserService userService;
    private final GoogleOAuth2Client googleOAuth2Client;

    public GoogleOAuth2Service(UserService userService, GoogleOAuth2Client googleOAuth2Client) {
        this.userService = userService;
        this.googleOAuth2Client = googleOAuth2Client;
    }

    public void handleGoogleOAuth2Callback(String authorizationCode) {
        // Используйте GoogleOAuth2Client для получения информации о пользователе
        UserInfoFromGoogle userInfo = googleOAuth2Client.getUserInfo(authorizationCode);

        // Передайте информацию о пользователе в UserService для сохранения в базе данных
        userService.saveUser(userInfo.getFullName(), userInfo.getEmail());

        // Выполните дополнительные операции или перенаправьте пользователя по необходимости
    }
}
