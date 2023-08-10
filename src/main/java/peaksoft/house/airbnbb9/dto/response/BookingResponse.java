package peaksoft.house.airbnbb9.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BookingResponse {
    private long bookingId;
    private List<AnnouncementResponse> announcementResponse;
}
