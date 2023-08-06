package peaksoft.house.airbnbb9.api.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.service.AnnouncementService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "Admin api", description = "")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminApi {

    private final AnnouncementService announcementService;

    @Operation(summary = "accepted", description = "admin accept application")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/accepted-announcement/{announcementId}")
    public SimpleResponse approveAnnouncement(@PathVariable Long announcementId) {
        return announcementService.approveAnnouncement(announcementId);
    }

    @Operation(summary = "rejected", description = "admin reject application")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/reject-announcement/{announcementId}")
    public SimpleResponse rejectAnnouncement(@PathVariable Long announcementId) {
        return announcementService.rejectAnnouncement(announcementId);
    }
}
