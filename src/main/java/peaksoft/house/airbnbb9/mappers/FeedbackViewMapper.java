package peaksoft.house.airbnbb9.mappers;

import org.springframework.stereotype.Component;
import peaksoft.house.airbnbb9.dto.response.FeedbackResponse;
import peaksoft.house.airbnbb9.entity.Feedback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class FeedbackViewMapper {

    public FeedbackResponse viewFeedback(Feedback request) {
        if (request == null) {
            return null;
        }
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
        return response;
    }
    public List<FeedbackResponse> viewFeedback(List<Feedback> requests) {
        List<FeedbackResponse> responses = new ArrayList<>();
        for (Feedback f : requests) {
            responses.add(viewFeedback(f));
        }
        return responses;
    }
}