package peaksoft.house.airbnbb9.api.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.PaginationAnnouncementResponse;
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
    @Operation(summary = "Accepted,Deleted,Rejected", description = "Admin accept,deleted,rejected application")
    @PostMapping("/accepted-announcement/{announcementId}")
    public SimpleResponse approveAnnouncement(@PathVariable Long announcementId,
                                              @RequestParam String value,
                                              @RequestParam(required = false)
                                              String messageFromAdminToUser) throws MessagingException {
        return announcementService.processAnnouncement(announcementId, value, messageFromAdminToUser);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "getById-application", description = "Admin at accepted,deleted,rejected application")
    @GetMapping("/applicationById")
    public AnnouncementResponse getByIdApplication(@RequestParam Long applicationId) {
        return announcementService.getApplicationById(applicationId);
    }

    @Operation(summary = "Get all announcements application and pagination",
            description = "Get all  announcements by status 'MODERATION' and added pagination")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/announcementsModeration")
    public PaginationAnnouncementResponse getAllAnnouncementsModerationAndPagination(@RequestParam(defaultValue = "1") int currentPage,
                                                                                     @RequestParam(defaultValue = "15") int pageSize) {
        return announcementService.getAllAnnouncementsModerationAndPagination(currentPage, pageSize);
    }
}
