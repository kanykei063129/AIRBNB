package peaksoft.house.airbnbb9.repository.template;

import peaksoft.house.airbnbb9.dto.response.FeedbackResponse;

import java.util.List;

public interface FeedbackTemplate {

    List<FeedbackResponse> getAllFeedback(Long announcementId);
}
