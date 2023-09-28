package peaksoft.house.airbnbb9.api.vendor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.request.FeedbackRequest;
import peaksoft.house.airbnbb9.dto.request.FeedbackUpdateRequest;
import peaksoft.house.airbnbb9.dto.response.FeedbackResponse;
import peaksoft.house.airbnbb9.dto.response.QuantityLikeAndDisLikeResponse;
import peaksoft.house.airbnbb9.dto.response.RatingCountResponse;
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
    @PostMapping("/{announcementId}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public SimpleResponse leaveFeedback(@PathVariable Long announcementId,@Valid @RequestBody FeedbackRequest request) {
        return feedbackService.saveFeedback(announcementId, request);
    }

    @Operation(summary = "Get all feedback", description = "Get all feedback by announcement id")
    @GetMapping("/{announcementId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<FeedbackResponse> getAllFeedback(@PathVariable Long announcementId) {
        return feedbackService.getAllFeedback(announcementId);
    }

    @Operation(summary = "Like and dislike feedbacks",
            description = "This method checks if user liked or disliked this feedback than counts them" +
                    "Each user can like or dislike a feedback only once")
    @PostMapping("/likeAndDislike")
    @PreAuthorize("hasAnyAuthority('USER')")
    public QuantityLikeAndDisLikeResponse likeAndDisLike(
            @RequestParam Long feedbackId,
            @RequestParam String likeOrDislike) throws AlreadyExistsException {
        return feedbackService.likeAndDisLike(feedbackId, likeOrDislike);
    }

    @Operation(summary = "Update feedback",
            description = "Update feedback by id")
    @PutMapping("/{feedbackId}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public SimpleResponse updateFeedback(@PathVariable Long feedbackId,
                                         @RequestBody FeedbackUpdateRequest feedbackUpdateRequest) {
        return feedbackService.updateFeedback(feedbackId, feedbackUpdateRequest);
    }

    @Operation(summary = "Delete feedback",
            description = "Delete feedback by id")
    @DeleteMapping ("/{feedbackId}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public SimpleResponse deleteFeedback(@PathVariable Long feedbackId) {
        return feedbackService.deleteFeedback(feedbackId);
    }

    @Operation(summary = "Count ratings",
            description = "Count ratings ")
    @GetMapping ("countRating/{announcementId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public RatingCountResponse countRating(@PathVariable Long announcementId) {
        return feedbackService.countRating(announcementId);
    }
}