
package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.dto.response.UserProfileResponse;
import peaksoft.house.airbnbb9.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    SimpleResponse deleteUserById(Long userId);
    UserProfileResponse getUserProfile();
}