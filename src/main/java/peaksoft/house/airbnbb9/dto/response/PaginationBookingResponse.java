package peaksoft.house.airbnbb9.dto.response;

import lombok.Builder;

import java.util.List;
@Builder
public class PaginationBookingResponse {
    private List<BookingResponse> bookingResponses;
    private int currentPage;
    private int pageSize;

}
