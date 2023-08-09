package peaksoft.house.airbnbb9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Status;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAnnouncementResponse {
    private Long id;
    private String image;
    private HouseType houseType;
    private int price;
    private Double rating;
    private String title;
    private String description;
    private String address;
    private Integer maxGuests;
    private Status status;
    private Integer bookingsCountAnnouncement;
    private String messagesFromAdmin;
}
