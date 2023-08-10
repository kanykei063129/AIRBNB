package peaksoft.house.airbnbb9.api.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.service.AnnouncementService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "Admin api", description = "API for admin management")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminApi {

    private final AnnouncementService announcementService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "accepted", description = "admin accept application")
    @PostMapping("/accepted-announcement/{announcementId}")
    public SimpleResponse approveAnnouncement(@PathVariable Long announcementId, @RequestParam String value) throws MessagingException {
        return announcementService.processAnnouncement(announcementId, value);
    }
}
