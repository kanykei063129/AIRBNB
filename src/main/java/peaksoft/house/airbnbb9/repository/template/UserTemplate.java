package peaksoft.house.airbnbb9.repository.template;

import peaksoft.house.airbnbb9.dto.response.UserResponse;

import java.util.List;

public interface UserTemplate {
    List<UserResponse> getAllUsers();
}
