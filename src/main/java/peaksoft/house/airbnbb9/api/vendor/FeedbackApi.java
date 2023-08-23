package peaksoft.house.airbnbb9.api.vendor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.request.FeedbackRequest;
import peaksoft.house.airbnbb9.dto.response.FeedbackResponse;
import peaksoft.house.airbnbb9.service.FeedbackService;

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
}
