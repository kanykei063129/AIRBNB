package peaksoft.house.airbnbb9.api.admin;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.response.*;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.service.AnnouncementService;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
@Tag(name = "Announcement Api", description = "All announcement endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnnouncementApi {

    private final AnnouncementService announcementService;

    @Operation(summary = "getAnnouncements", description = "Get all announcements")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/announcements")
    public List<AnnouncementResponse> getAllAnnouncement() {
        return announcementService.getAllAnnouncements();
    }

    @Operation(summary = "update by id", description = "Update announcement by id ")
    @PutMapping("/{announcementId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AnnouncementResponse update(@PathVariable Long announcementId, @RequestBody @Valid AnnouncementRequest announcementRequest) {
        return announcementService.updateAnnouncement(announcementId, announcementRequest);
    }

    @Operation(summary = "delete Announcement By id", description = "Delete announcement by id ")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{announcementId}")
    public SimpleResponse deleteByIdAnnouncement(@PathVariable Long announcementId) {
        return announcementService.deleteByIdAnnouncement(announcementId);
    }

    @Operation(summary = "getAnnouncementByID", description = "Get announcement by id ")
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/getById{announcementId}")
    public AllAnnouncementResponse getAnnouncementById(@PathVariable Long announcementId) {
        return announcementService.getByIdAnnouncement(announcementId);
    }

    @Operation(summary = "filter and sort announcements", description = " filter  announcements by status and house type ")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/announcements-filter")
    public List<AnnouncementResponse> getAllAnnouncementsFilterAndSort(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) HouseType houseType,
            @RequestParam(required = false) String rating,
            @RequestParam(required = false) String price) {
        return announcementService.getAllAnnouncementsFilter(status, houseType, rating, price);
    }
    @Operation(summary = "get all announcements moderation and pagination", description = "Get all  announcements by status 'MODERATION' and pagination")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/announcementsModeration")
    public PaginationAnnouncementResponse getAllAnnouncementsModerationAndPagination(@RequestParam int currentPage, @RequestParam int pageSize) {
        return announcementService.getAllAnnouncementsModerationAndPagination(currentPage, pageSize);
    }
    @Operation(summary = "Any user can filter announcements", description = "Filter accepted announcements by Popular,House Type, and Price Low to High and High to Low")
    @GetMapping("/filter")
    public List<AnnouncementResponse> getAllAnnouncementsFilters(
            @RequestParam(required = false) HouseType houseType,
            @RequestParam(required = false) String rating,
            @RequestParam(required = false) String price) {
        return announcementService.getAllAnnouncementsFilters(houseType, rating, price);
    }
}
