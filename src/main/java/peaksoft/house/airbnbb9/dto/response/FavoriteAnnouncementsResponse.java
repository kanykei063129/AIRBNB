package peaksoft.house.airbnbb9.dto.response;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import peaksoft.house.airbnbb9.enums.Status;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteAnnouncementsResponse {
    private Long id;
    private String image;
    private int price;
    private String address;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    private int maxGuests;
    private double rating;
    private boolean isFavorite;
}
