package peaksoft.house.airbnbb9.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
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
    private boolean isFavorite;
}
