package peaksoft.house.airbnbb9.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private LocalDate date;
    private HttpStatus httpStatus;
    private String message;
}
