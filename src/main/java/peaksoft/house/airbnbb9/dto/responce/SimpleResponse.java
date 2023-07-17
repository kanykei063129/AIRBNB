package peaksoft.house.airbnbb9.dto.responce;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class SimpleResponse {
    private String message;
    private HttpStatus httpStatus;
    @Builder
    public SimpleResponse(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
