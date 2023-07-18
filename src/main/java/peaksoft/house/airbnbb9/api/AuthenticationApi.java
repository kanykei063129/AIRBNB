package peaksoft.house.airbnbb9.api;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.request.SignInRequest;
import peaksoft.house.airbnbb9.dto.request.UserRequest;
import peaksoft.house.airbnbb9.dto.responce.AuthenticationResponse;
import peaksoft.house.airbnbb9.dto.responce.UserResponse;
import peaksoft.house.airbnbb9.service.AuthenticationService;
import peaksoft.house.airbnbb9.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationApi {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/signup")
    public UserResponse signUp(@RequestBody UserRequest userRequest) {
        return userService.registerUser(userRequest);
    }

    @PostMapping("/signIn")
    public AuthenticationResponse signIn(@RequestBody SignInRequest request) {
        return authenticationService.signIn(request);
    }


}
