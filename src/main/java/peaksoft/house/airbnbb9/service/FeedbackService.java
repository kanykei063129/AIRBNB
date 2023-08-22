package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.request.FeedbackRequest;
import peaksoft.house.airbnbb9.dto.response.FeedbackResponse;

public interface FeedbackService {
    FeedbackResponse saveFeedback(Long announcementId, FeedbackRequest request);
}
