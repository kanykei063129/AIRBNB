package peaksoft.house.airbnbb9.dto.responce;

import lombok.*;
import org.springframework.http.HttpStatus;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleResponse {
    private String message;
    private HttpStatus httpStatus;
}
