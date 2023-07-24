package peaksoft.house.airbnbb9.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peaksoft.house.airbnbb9.dto.AuthenticationResponse;
import peaksoft.house.airbnbb9.dto.SignInRequest;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApi {
   private final UserService userService;

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody SignInRequest request) {
        return userService.authenticate(request);
    }

}
