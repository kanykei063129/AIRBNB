package peaksoft.house.airbnbb9.dto.responce;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private Integer rating;



    public AnnouncementResponse(long id, int price, int maxGuests, String address, String title, List<String> images, Status status) {
        this.id = id;
        this.address = address;
        this.images = images;
        this.price = price;
        this.maxGuests = maxGuests;
        this.title = title;
        this.status=status;
    }

    public AnnouncementResponse(long id, int price, int maxGuests, String address, String title, List<String> images, Status status, Integer rating) {
        this.id = id;
        this.address = address;
        this.images = images;
        this.price = price;
        this.maxGuests = maxGuests;
        this.title = title;
        this.status=status;
        this.rating=rating;
    }
}
