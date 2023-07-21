package peaksoft.house.airbnbb9.api;

import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Google authentication", description = "Any user can authenticate with Google")
    @PostMapping("/google")
    public AuthenticationResponse sigInWithGoogle(@RequestParam String tokenId) throws FirebaseAuthException {
        return authenticationService.signInWithGoogle(tokenId);
    }

    @PostMapping("/signIn")
    public AuthenticationResponse signIn(@RequestBody SignInRequest request) {
        return authenticationService.signIn(request);
    }

}