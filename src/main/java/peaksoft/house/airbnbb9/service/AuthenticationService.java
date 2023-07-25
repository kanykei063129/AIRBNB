package peaksoft.house.airbnbb9.service;

import com.google.firebase.auth.FirebaseAuthException;
import peaksoft.house.airbnbb9.dto.request.SignInRequest;
import peaksoft.house.airbnbb9.dto.responce.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse signInWithGoogle(String tokenId) throws FirebaseAuthException;
    AuthenticationResponse signIn(SignInRequest request);
}
