package peaksoft.house.airbnbb9.service;

import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.request.UserRequest;
import peaksoft.house.airbnbb9.dto.responce.UserResponse;
import peaksoft.house.airbnbb9.entity.User;

@Service
public interface UserService {

    public UserResponse registerUser(UserRequest userRequest);
    // Perform user registration and save user details to the database


    public UserResponse processGoogleLogin(String code);
    // Handle Google Sign-In callback and retrieve user information using the code
    // Validate the code and exchange it for an access token using the client ID and secret
    // Retrieve user details from Google using the access token
    // Create a user account and save user details to the database
    public String getGoogleSignInUrl();
}
