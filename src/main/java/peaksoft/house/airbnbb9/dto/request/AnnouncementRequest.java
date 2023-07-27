package peaksoft.house.airbnbb9.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
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
    @NotNull(message = "HouseType must not be null!")
    private HouseType houseType;

    @ElementCollection
    @NotNull(message = "Images must not be empty!")
    private List<String> images;
    @NotNull(message = "Price must not be null!")
    @Positive(message = "Price must be a positive number!")
    private int price;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Region must not be null!")
    private Region region;
    @NotNull(message = "Address must not be null!")
    private String address;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;
    private String title;
    private int maxGuests;
    private String province;
}
