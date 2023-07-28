package peaksoft.house.airbnbb9.dto.response;

import lombok.Builder;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

@Builder
public record AnnouncementResponse(
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
        String province
) {
    public AnnouncementResponse(Long id, HouseType houseType, List<String> images, int price, Region region, String address, String description, Status status, String title, int maxGuests, String province) {
        this.id = id;
        this.houseType = houseType;
        this.images = images;
        this.price = price;
        this.region = region;
        this.address = address;
        this.description = description;
        this.status = status;
        this.title = title;
        this.maxGuests = maxGuests;
        this.province = province;
    }
}
