package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.AuthenticationResponse;
import peaksoft.house.airbnbb9.dto.SignInRequest;
import peaksoft.house.airbnbb9.dto.response.AllAnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;

public interface UserService {
    AuthenticationResponse authenticate(SignInRequest request);
}
