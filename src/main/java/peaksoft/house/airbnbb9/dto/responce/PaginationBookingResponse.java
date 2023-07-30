package peaksoft.house.airbnbb9.dto.responce;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;
@Builder
public class PaginationBookingResponse {
    private List<BookingResponse> bookingResponses;
    private int currentPage;
    private int pageSize;

}
