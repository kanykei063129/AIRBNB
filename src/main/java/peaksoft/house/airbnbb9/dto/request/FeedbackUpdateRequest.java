package peaksoft.house.airbnbb9.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class FeedbackUpdateRequest {
    private String comment;
    private List<String>images;
}
