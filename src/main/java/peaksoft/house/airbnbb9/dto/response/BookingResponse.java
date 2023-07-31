package peaksoft.house.airbnbb9.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private Long userId;
    private Long announcementId;


    public BookingResponse(Long bookingId, Long userId, Long announcementId) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.announcementId = announcementId;
    }
}
