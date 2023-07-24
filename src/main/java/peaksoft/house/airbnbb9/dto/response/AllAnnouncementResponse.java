package peaksoft.house.airbnbb9.dto.response;

import lombok.Builder;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

@Builder
public record AllAnnouncementResponse(
        Long id,
        HouseType houseType,
        List<String> images,
        int price,
        Region region,
        String address,
        String description,
        Status status,
        String title,
        int maxGuests,
        String province,
        int isFavorite
) {
}
