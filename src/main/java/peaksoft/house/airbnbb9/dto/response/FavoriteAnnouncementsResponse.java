package peaksoft.house.airbnbb9.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteAnnouncementsResponse {
    private Long id;
    private List<String> images;
    private int price;
    private String address;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    private int maxGuests;
    private double rating;
    private boolean isFavorite;
}