package peaksoft.house.airbnbb9.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class FeedbackResponse {
    private Long id;
    private String feedbackUserImage;
    private String feedbackUserFullName;
    private Integer rating;
    private String comment;
    private List<String> images;
    private LocalDate createdAt;
    private Integer likeCount;
    private Integer disLikeCount;
}
