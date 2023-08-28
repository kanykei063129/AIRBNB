package peaksoft.house.airbnbb9.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class FeedbackRequest {
    private List<String> images;
    private int rating;
    private String comment;
}
