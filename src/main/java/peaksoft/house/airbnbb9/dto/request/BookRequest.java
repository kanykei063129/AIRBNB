package peaksoft.house.airbnbb9.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookRequest {
    private Long announcementId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    @Min(1)
    private double amount;
    private String token;
}
