package peaksoft.house.airbnbb9.api.vendor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.request.FeedbackRequest;
import peaksoft.house.airbnbb9.dto.request.FeedbackUpdateRequest;
import peaksoft.house.airbnbb9.dto.response.FeedbackResponse;
import peaksoft.house.airbnbb9.dto.response.QuantityLikeAndDisLikeResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.exception.AlreadyExistsException;
import peaksoft.house.airbnbb9.service.FeedbackService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedbacks")
@Tag(name = "Feedback api", description = "Only available for user")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAnyAuthority('USER')")
public class FeedbackApi {

    private final FeedbackService feedbackService;

    @Operation(summary = "Leave feedback", description = "Leave feedback to announcement")
    @PostMapping("/{announcementId}")
    public FeedbackResponse leaveFeedback(@PathVariable Long announcementId, @RequestBody FeedbackRequest request) {
        return feedbackService.saveFeedback(announcementId, request);
    }

    @Operation(summary = "Get all feedback", description = "Get all feedback by announcement id")
    @GetMapping("/{announcementId}")
    public List<FeedbackResponse> getAllFeedback(@PathVariable Long announcementId) {
        return feedbackService.getAllFeedback(announcementId);
    }

    @Operation(summary = "Like and dislike feedbacks",
            description = "This method checks if user liked or disliked this feedback than counts them" +
                    "Each user can like or dislike a feedback only once")
    @PostMapping("/likeAndDislike")
    public QuantityLikeAndDisLikeResponse likeAndDisLike(
            @RequestParam Long feedbackId,
            @RequestParam String likeOrDislike) throws AlreadyExistsException {
        return feedbackService.likeAndDisLike(feedbackId, likeOrDislike);
    }

    @Operation(summary = "Update feedback",
            description = "Update feedback by id")
    @PutMapping("/{feedbackId}")
    public SimpleResponse updateFeedback(@PathVariable Long feedbackId,
                                         @RequestBody FeedbackUpdateRequest feedbackUpdateRequest) {
        return feedbackService.updateFeedback(feedbackId, feedbackUpdateRequest);
    }

    @Operation(summary = "Deleted feedback",
            description = "Deleted feedback by id")
    @DeleteMapping ("/{feedbackId}")
    public SimpleResponse deleteFeedback(@PathVariable Long feedbackId) {
        return feedbackService.deleteFeedback(feedbackId);
    }
}