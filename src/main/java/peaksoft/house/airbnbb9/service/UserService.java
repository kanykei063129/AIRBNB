
package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
}