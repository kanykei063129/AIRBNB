package peaksoft.house.airbnbb9.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peaksoft.house.airbnbb9.enums.Status;

@Getter
@Setter
@NoArgsConstructor
public class UserBookingsResponse {
    private Long announcementId;
    private String image;
    private Double rating;
    private String title;
    private String address;
    private Integer maxGuests;
    private String checkIn;
    private String checkOut;
    private Status status;
}
