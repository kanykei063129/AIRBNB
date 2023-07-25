package peaksoft.house.airbnbb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.SimpleResponse;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.AllAnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.service.AnnouncementService;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
@Tag(name = "Announcement Api",description = "All announcement endpoints")
public class AnnouncementApi {
    private final AnnouncementService announcementService;

    @Operation(summary = "getAnnouncementByID",description = "Get announcement by id ")
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/getById{announcementId}")
    public AllAnnouncementResponse getAnnouncementById(@PathVariable Long announcementId) {
        return announcementService.getByIdAnnouncement(announcementId);
    }
    @Operation(summary = "getAnnouncements",description = "Get all announcements")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAllAnnouncements")
    public List<AnnouncementResponse> getAllAnnouncement() {
        return announcementService.getAllAnnouncements();
    }

    @Operation(summary = "update By id",description = "Update announcement by id ")
    @PermitAll
    @PutMapping("/update/{announcementId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AnnouncementResponse update(@PathVariable Long announcementId, @RequestBody @Valid AnnouncementRequest announcementRequest) {
        return announcementService.updateAnnouncement(announcementId, announcementRequest);
    }
    @Operation(summary = "delete Announcement By id",description = "Delete announcement by id ")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{announcementId}")
    public SimpleResponse deleteByIdAnnouncement(@PathVariable Long announcementId) {
        return announcementService.deleteByIdAnnouncement(announcementId);
    }
}
