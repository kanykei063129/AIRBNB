package peaksoft.house.airbnbb9.api.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.*;
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

    @Operation(summary = "Get announcements",
            description = "Get all announcements")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/announcements")
    public List<AnnouncementResponse> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    @Operation(summary = "Update announcement",
            description = "Update announcement by id")
    @PutMapping("/{announcementId}")
    @PreAuthorize("hasAuthority('USER')")
    public AnnouncementResponse update(@PathVariable Long announcementId,
                                       @RequestBody @Valid AnnouncementRequest announcementRequest) {
        return announcementService.updateAnnouncement(announcementId, announcementRequest);
    }

    @Operation(summary = "Delete announcement",
            description = "Delete announcement by id")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @DeleteMapping("/{announcementId}")
    public SimpleResponse deleteByIdAnnouncement(@PathVariable Long announcementId) {
        return announcementService.deleteByIdAnnouncement(announcementId);
    }

    @Operation(summary = "Find an announcement by id", description = "Any user can find announcement by id")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("{id}")
    public AnnouncementInnerPageResponse getAnnouncementDetails(@PathVariable Long id) {
        return announcementService.getAnnouncementDetails(id);
    }

    @Operation(summary = "Filter and sort announcements",
            description = "Filter  announcements by status,house type,rating and price")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/announcements-filter")
    public List<AnnouncementResponse> getAllAnnouncementsFilterAndSort(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) HouseType houseType,
            @RequestParam(required = false) String rating,
            @RequestParam(required = false) String price) {
        return announcementService.getAllAnnouncementsFilter(status, houseType, rating, price);
    }

    @Operation(summary = "Get all announcements application and pagination",
            description = "Get all  announcements by status 'MODERATION' and added pagination")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/announcementsModeration")
    public PaginationAnnouncementResponse getAllAnnouncementsModerationAndPagination(@RequestParam(defaultValue = "1") int currentPage,
                                                                                     @RequestParam(defaultValue = "15") int pageSize) {
        return announcementService.getAllAnnouncementsModerationAndPagination(currentPage, pageSize);
    }

    @Operation(summary = "Pagination for get all announcements",
            description = "Announcement can be received with pagination and without pagination")
    @GetMapping("/pagination")
    public PaginationAnnouncementResponse paginations(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return announcementService.pagination(page, size);
    }

    @Operation(summary = "Get latest announcement", description = "Get latest announcement by create date")
    @GetMapping("/latestAnnouncement")
    public LatestAnnouncementResponse getLatestAnnouncement() {
        return announcementService.getLatestAnnouncement();
    }

    @Operation(summary = "Get popular houses", description = "Get popular houses by rating")
    @GetMapping("/getPopularHouses")
    public List<PopularHouseResponse> getPopularHouses() {
        return announcementService.getPopularHouses();
    }

    @Operation(summary = "Get popular apartment ", description = "Get popular apartment by rating")
    @GetMapping("/getPopularApartment")
    public PopularApartmentResponse getPopularApartment() {
        return announcementService.getPopularApartment();
    }
}
