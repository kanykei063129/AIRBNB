package peaksoft.house.airbnbb9.dto.request;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class AnnouncementRequest {
    @Enumerated(EnumType.STRING)
    private HouseType houseType;

    @ElementCollection
    private List<String> images;
    private int price;

    @Enumerated(EnumType.STRING)
    private Region region;
    private String address;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;
    private String title;
    private int maxGuests;
    private String province;
}
