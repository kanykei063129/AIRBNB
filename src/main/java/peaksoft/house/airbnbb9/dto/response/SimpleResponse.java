package peaksoft.house.airbnbb9.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;
@Builder
public class SimpleResponse {
    private String message;
    private HttpStatus httpStatus;
}
