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
public class FeedbackApi {

    private final FeedbackService feedbackService;

    @Operation(summary = "Leave feedback", description = "Leave feedback to announcement")
    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("leave/{announcementId}")
    public FeedbackResponse leaveFeedback(@PathVariable Long announcementId, @RequestBody FeedbackRequest request) {
        return feedbackService.saveFeedback(announcementId, request);
    }

    @Operation(summary = "Get all feedback", description = "Get all feedback by announcement id")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/{announcementId}")
    public List<FeedbackResponse> getAllFeedback(@PathVariable Long announcementId) {
        return feedbackService.getAllFeedback(announcementId);
    }

    @Operation(summary = "Like and dislike",
            description = "The purpose of the method is to manage and update the number of likes and dislikes for a particular review based on user actions. " +
                    "It is also necessary that each user can like or dislike a review only once")
    @PostMapping("/likeAndDislike")
    @PreAuthorize("hasAuthority('USER')")
    public QuantityLikeAndDisLikeResponse likeAndDisLike(
            @RequestParam Long feedbackId,
            @RequestParam String likeOrDislike) throws AlreadyExistsException {
        return feedbackService.likeAndDisLike(feedbackId, likeOrDislike);
    }

    @Operation(summary = "Update feedback",
            description = "Update feedback by id")
    @PostMapping("/update/{feedbackId}")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse updateFeedback(@PathVariable Long feedbackId,
                                         @RequestBody FeedbackUpdateRequest feedbackUpdateRequest) {
        return feedbackService.updateFeedback(feedbackId, feedbackUpdateRequest);
    }

    @Operation(summary = "Deleted feedback",
            description = "Deleted feedback by id")
    @PostMapping("/delete/{announcementId}/{feedbackId}")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse deleteFeedback(@PathVariable Long announcementId,
                                         @PathVariable Long feedbackId) {
        return feedbackService.deleteFeedback(announcementId,feedbackId);
    }
}