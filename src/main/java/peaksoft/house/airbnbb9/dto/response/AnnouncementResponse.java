package peaksoft.house.airbnbb9.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AnnouncementResponse {
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
    private User user;


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

    public AnnouncementResponse(long id, int price, int maxGuests, String address, String title, List<String> images, Status status) {
        this.id = id;
        this.address = address;
        this.images = images;
        this.price = price;
        this.maxGuests = maxGuests;
        this.title = title;
        this.status = status;
    }

    public AnnouncementResponse(long id, int price, int maxGuests, String address, String title, List<String> images, Status status, double rating) {
        this.id = id;
        this.address = address;
        this.images = images;
        this.price = price;
        this.maxGuests = maxGuests;
        this.title = title;
        this.status = status;
        this.rating = rating;
    }

    public AnnouncementResponse(Long id, List<String> images, int price, Region region, String address, String description, int maxGuests) {
        this.id = id;
        this.images = images;
        this.price = price;
        this.region = region;
        this.address = address;
        this.description = description;
        this.maxGuests = maxGuests;
    }

    public AnnouncementResponse(Long id, List<String> images, int price, Region region, String address, String description, String title, int maxGuests, String province, double rating) {
        this.id = id;
        this.images = images;
        this.price = price;
        this.region = region;
        this.address = address;
        this.description = description;
        this.title = title;
        this.maxGuests = maxGuests;
        this.province = province;
        this.rating = rating;
    }

}
