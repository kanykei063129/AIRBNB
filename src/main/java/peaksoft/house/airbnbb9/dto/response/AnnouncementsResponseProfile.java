package peaksoft.house.airbnbb9.dto.response;

import lombok.*;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.HouseType;

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
    private HouseType houseType;
    private int maxGuests;
    private String address;
    private String description;
    private String fullName;
    private String email;
    private String image;
    private User user;
    private List<String> bookedByFullName;
    private List<String> bookedByEmail;
    private List<String> favoriteByFullName;
    private List<String> favoriteByEmail;
}
