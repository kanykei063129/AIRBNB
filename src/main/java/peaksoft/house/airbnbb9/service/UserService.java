
package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.response.FilterResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.dto.response.UserProfileResponse;
import peaksoft.house.airbnbb9.dto.response.UserResponse;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.PriceType;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    SimpleResponse deleteUserById(Long userId);

    UserProfileResponse getUserProfile();
    FilterResponse getAllAnnouncementsFilters(HouseType houseType, String rating, PriceType price);

    UserResponse getByIdUser(Long userId,String value);
}