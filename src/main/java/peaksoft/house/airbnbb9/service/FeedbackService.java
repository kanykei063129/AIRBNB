package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.request.FeedbackRequest;
import peaksoft.house.airbnbb9.dto.request.FeedbackUpdateRequest;
import peaksoft.house.airbnbb9.dto.response.FeedbackResponse;
import peaksoft.house.airbnbb9.dto.response.QuantityLikeAndDisLikeResponse;
import peaksoft.house.airbnbb9.dto.response.RatingCountResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.exception.AlreadyExistsException;

import java.util.List;

public interface
FeedbackService {
    SimpleResponse saveFeedback(Long announcementId, FeedbackRequest request);

    List<FeedbackResponse> getAllFeedback(Long announcementId);

    QuantityLikeAndDisLikeResponse likeAndDisLike (Long feedbackId,String likeOrDislike) throws AlreadyExistsException;

    SimpleResponse updateFeedback(Long feedbackId, FeedbackUpdateRequest feedbackUpdateRequest);

    SimpleResponse deleteFeedback(Long feedbackId);

    RatingCountResponse countRating(Long announcementId);
}
