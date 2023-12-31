package peaksoft.house.airbnbb9.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleResponse {
    private String message;
    private HttpStatus httpStatus;
}
