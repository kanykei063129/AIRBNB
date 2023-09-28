package peaksoft.house.airbnbb9.dto.response;

import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FeedbackResponse {
    private Long id;
    private String feedbackUserFullName;
    private String feedbackUserImage;
    private Integer rating;
    private String comment;
    private List<String> images;
    private Date createdAt;
    private Integer likeCount;
    private Integer disLikeCount;
}