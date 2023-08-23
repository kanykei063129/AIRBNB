package peaksoft.house.airbnbb9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAnnouncementResponse {
    private Long id;
    private List<String> images;
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
