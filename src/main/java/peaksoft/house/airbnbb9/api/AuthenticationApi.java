package peaksoft.house.airbnbb9.api;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.request.SignInRequest;
import peaksoft.house.airbnbb9.dto.responce.AuthenticationResponse;
import peaksoft.house.airbnbb9.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationApi {
    private final AuthenticationService authenticationService;

    @PostMapping("/signIn")
    public AuthenticationResponse signIn(@RequestBody SignInRequest request) {
        return authenticationService.signIn(request);
    }
}
