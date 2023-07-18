package peaksoft.house.airbnbb9.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleOAuth2Client {

    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;


    public UserInfoFromGoogle getUserInfo(String authorizationCode) {
        // Обмен кода авторизации на токен доступа
        String accessToken = exchangeAuthorizationCodeForAccessToken(authorizationCode);

        // Получение информации о пользователе от сервиса Google UserInfo
        return fetchUserInfo(accessToken);
    }

    private String exchangeAuthorizationCodeForAccessToken(String authorizationCode) {
        // Подготовка заголовков и тела запроса
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("code", authorizationCode);
        requestBody.put("client_id", clientId);
        requestBody.put("client_secret", clientSecret);
        requestBody.put("redirect_uri", "http://localhost:8080/oauth2/google/callback");
        requestBody.put("grant_type", "authorization_code");

        // Создание запроса на обмен кода авторизации на токен доступа
        HttpEntity requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL, requestEntity, Map.class);
        Map<String, Object> responseBody = responseEntity.getBody();

        // Получение токена доступа из ответа
        return (String) responseBody.get("access_token");
    }

    private UserInfoFromGoogle fetchUserInfo(String accessToken) {
        // Создание заголовка с токеном доступа
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Отправка запроса на получение информации о пользователе
        ResponseEntity<UserInfoFromGoogle> responseEntity = restTemplate.exchange(
                GOOGLE_USERINFO_URL,
                HttpMethod.GET,
                requestEntity,
                UserInfoFromGoogle.class
        );

        // Возвращение объекта с информацией о пользователе
        return responseEntity.getBody();
    }
}
