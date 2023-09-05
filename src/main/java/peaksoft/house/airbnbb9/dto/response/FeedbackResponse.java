package peaksoft.house.airbnbb9.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
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