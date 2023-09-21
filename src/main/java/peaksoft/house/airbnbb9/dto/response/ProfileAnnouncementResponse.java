package peaksoft.house.airbnbb9.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class ProfileAnnouncementResponse {
    private Long id;
    private HouseType houseType;
    private List<String> images;
    private int price;
    private Region region;
    private String address;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String title;
    private int maxGuests;
    private String province;
    private double rating;

    public ProfileAnnouncementResponse(Long id, HouseType houseType, List<String> images, int price, Region region, String address, String description, Status status, String title, int maxGuests, String province, double rating) {
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
        this.rating = rating;
    }
}
