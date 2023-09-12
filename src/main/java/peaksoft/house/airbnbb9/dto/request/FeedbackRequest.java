package peaksoft.house.airbnbb9.dto.request;

import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class FeedbackRequest {

    private List<String> images;

    @Max(value = 5,message = "Maximum score 5")
    private int rating;
    private String comment;
}
