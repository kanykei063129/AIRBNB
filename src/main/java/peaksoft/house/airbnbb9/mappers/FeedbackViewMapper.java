package peaksoft.house.airbnbb9.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import peaksoft.house.airbnbb9.dto.response.FeedbackResponse;
import peaksoft.house.airbnbb9.entity.Feedback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class FeedbackViewMapper {

    public FeedbackResponse viewFeedback(Feedback request) {
        if (request == null) {
            return null;
        }
        log.info("Creating FeedbackResponse for feedback ID: " + request.getId());

        FeedbackResponse response = new FeedbackResponse();
        response.setId(request.getId());
        response.setFeedbackUserImage(request.getUser().getImage());
        response.setFeedbackUserFullName(request.getUser().getFullName());
        response.setRating(request.getRating());
        response.setComment(request.getComment());
        response.setImages(request.getImages());
        response.setCreatedAt(LocalDate.from(request.getCreateDate()));
        response.setLikeCount(response.getLikeCount());
        response.setDisLikeCount(response.getDisLikeCount());

        log.info("Created FeedbackResponse successfully for feedback ID: " + request.getId());
        return response;
    }
    public List<FeedbackResponse> viewFeedback(List<Feedback> requests) {
        List<FeedbackResponse> responses = new ArrayList<>();

        log.info("Creating FeedbackResponses for a list of feedback");

        for (Feedback f : requests) {
            responses.add(viewFeedback(f));
        }
        log.info("Created FeedbackResponses successfully for the list of feedback");
        return responses;
    }
}