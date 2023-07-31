package peaksoft.house.airbnbb9.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.response.*;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import peaksoft.house.airbnbb9.dto.response.AllAnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.service.AnnouncementService;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
@Tag(name = "Announcement Api",description = "All announcement endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnnouncementApi {
    private final AnnouncementService announcementService;
    @Operation(summary = "getAnnouncements",description = "Get all announcements")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAnnouncements")
    public List<AnnouncementResponse> getAllAnnouncement() {
        return announcementService.getAllAnnouncements();
    }
    @Operation(summary = "update by id",description = "Update announcement by id ")
    @PutMapping("/{announcementId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AnnouncementResponse update(@PathVariable Long announcementId, @RequestBody @Valid AnnouncementRequest announcementRequest) {
        return announcementService.updateAnnouncement(announcementId, announcementRequest);
    }
    @Operation(summary = "delete Announcement By id",description = "Delete announcement by id ")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{announcementId}")
    public SimpleResponse deleteByIdAnnouncement(@PathVariable Long announcementId) {
        return announcementService.deleteByIdAnnouncement(announcementId);
    }

    @Operation(summary = "getAnnouncementByID",description = "Get announcement by id ")
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/getById{announcementId}")
    public AllAnnouncementResponse getAnnouncementById(@PathVariable Long announcementId) {
        return announcementService.getByIdAnnouncement(announcementId);
    }

    @GetMapping("/filterByStatus")
    public List<AnnouncementResponse> getAllAnnouncementsFilterByStatus(@RequestParam(name = "FilterByStatus") Status status) {
        if (status == null) {
            return null;
        } else {
            return announcementService.getAllAnnouncementsFilterByStatus(status);
        }
    }

    @GetMapping("sortByPopular")
    public List<AnnouncementResponse> getAllAnnouncementsSortByRating(@RequestParam(name = "sortByRating") String popular) {
        if (popular == null) {
            return null;
        } else if (popular.equals("popular")) {
            return announcementService.getAllAnnouncementsThePopular(popular);
        } else
            return announcementService.getAllAnnouncementsTheLasted();
    }

    @GetMapping("/filterByHouseType")
    public List<AnnouncementResponse> getAllAnnouncementsFilterByHouseType(@RequestParam(name = "FilterByHouseType") HouseType houseType) {
        if (houseType == null) {
            return null;
        } else {
            return announcementService.getAllAnnouncementsFilterByHomeType(houseType);
        }
    }

    @GetMapping("filterByPrice")
    public List<AnnouncementResponse> getAllAnnouncementsSortByPrice(@RequestParam(name = "filterByPrice") String highToLow) {
        if (highToLow == null) {
            return null;
        } else if (highToLow.equals("highToLow")) {
            return announcementService.getAllAnnouncementsFilterByPriceHighToLow(highToLow);
        } else
            return announcementService.getAllAnnouncementsFilterByPriceLowToHigh();
    }
    @Operation(summary = "getAllAnnouncementsBookings",description = "Get all announcements bookings")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getAllAnnouncementsBookings/{userId}")
    public List<BookingResponse> getAllAnnouncementBookings(@PathVariable Long userId) {
        return announcementService.getAllAnnouncementsBookings(userId);
    }
    @Operation(summary = "get My Announcements",description = "Get My announcements")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getAllMyAnnouncements/{userId}")
    public List<AnnouncementResponse> getAllMyAnnouncement(@PathVariable Long userId) {
        return announcementService.getAllMyAnnouncements(userId);
    }
    @Operation(summary = "get_All_Announcements_Bookings_Sort_And_Pagination",description = "Get all announcements bookings,Sort And Pagination")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/announcementsBookings")
    public List<PaginationBookingResponse> getAllAnnouncementBookingsSortAndPagination(@RequestParam String ascOrDesc, @RequestParam int currentPage, @RequestParam int pageSize) {
        return announcementService.getAllAnnouncementsBookingsSortAndPagination(ascOrDesc, currentPage, pageSize);
    }
    @Operation(summary = "get_All_My Announcements Sort_And_Pagination",description = "Get All My announcements,Sort And Pagination")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/myAnnouncements")
    public List<PaginationAnnouncementResponse> getAllMyAnnouncementsSortAndPagination(@RequestParam String ascOrDesc, @RequestParam int currentPage, @RequestParam int pageSize) {
        return announcementService.getAllMyAnnouncementsSortAndPagination(ascOrDesc, currentPage, pageSize);
    }
}
