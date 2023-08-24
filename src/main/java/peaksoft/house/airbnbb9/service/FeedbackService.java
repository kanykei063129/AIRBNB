package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.request.FeedbackRequest;
import peaksoft.house.airbnbb9.dto.response.FeedbackResponse;
import peaksoft.house.airbnbb9.dto.response.QuantityLikeAndDisLikeResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;

import java.util.List;

public interface FeedbackService {
    FeedbackResponse saveFeedback(Long announcementId, FeedbackRequest request);

    List<FeedbackResponse> getAllFeedback(Long announcementId);

    QuantityLikeAndDisLikeResponse likeAndDisLike (Long feedbackId,String likeOrDislike);

    QuantityLikeAndDisLikeResponse countDisLike(Long feedbackId);
}
