package peaksoft.house.airbnbb9.dto.response;

import lombok.*;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserBookingsResponse {
    private Long announcementId;
    private List<String> images;
    private Double rating;
    private String title;
    private String address;
    private Integer maxGuests;
    private Region region;
    private String checkIn;
    private String checkOut;
}