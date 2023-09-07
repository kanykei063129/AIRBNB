package peaksoft.house.airbnbb9.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateBookRequest {
    @Min(1)
    private double amount;
    private Long announcementId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Long bookingId;
    private String token;
}
