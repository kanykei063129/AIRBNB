package peaksoft.house.airbnbb9.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

@Builder
public record AnnouncementRequest(
        @NotNull(message = "HouseType must not be null!")
        HouseType houseType,
        @NotNull(message = "Images must not be empty!")
        List<String> images,
        @NotNull(message = "Price must not be null!")
        @Positive(message = "Price must be a positive number!")
        int price,
        @NotNull(message = "Region must not be null!")
        Region region,
        @NotNull(message = "Address must not be null!")
        String address,
        String description,
        Status status,
        String title,
        int maxGuests,
        String province
) {
}
