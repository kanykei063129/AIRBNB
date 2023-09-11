package peaksoft.house.airbnbb9.dto.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementsResponseProfile {
    private Long id;
    private String title;
    private List<String> images;
    private String houseType;
    private int maxGuests;
    private String address;
    private String description;
    private String fullName;
    private String email;
    private String image;
    private List<String> bookedByFullName;
    private List<String> bookedByEmail;
    private List<String> favoriteByFullName;
    private List<String> favoriteByEmail;

}
