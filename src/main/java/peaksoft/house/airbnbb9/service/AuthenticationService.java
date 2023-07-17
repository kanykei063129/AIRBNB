package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.request.SignInRequest;
import peaksoft.house.airbnbb9.dto.responce.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse signIn(SignInRequest request);
}
