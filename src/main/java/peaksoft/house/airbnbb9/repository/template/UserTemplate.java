package peaksoft.house.airbnbb9.repository.template;

import peaksoft.house.airbnbb9.dto.response.FilterResponse;
import peaksoft.house.airbnbb9.dto.response.UserResponse;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.PriceType;

import java.util.List;

public interface UserTemplate {

    List<UserResponse> getAllUsers();

    UserResponse getByIdUser(Long userId,String value);

    FilterResponse getAllAnnouncementsFilters(HouseType houseType, String rating, PriceType price);
}
