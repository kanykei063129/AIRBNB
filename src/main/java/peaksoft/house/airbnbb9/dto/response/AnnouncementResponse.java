package peaksoft.house.airbnbb9.dto.response;


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
}
