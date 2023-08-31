package peaksoft.house.airbnbb9.api.vendor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.request.BookRequest;
import peaksoft.house.airbnbb9.dto.request.UpdateBookRequest;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.GetAnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.GlobalSearchResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.service.AnnouncementService;
import peaksoft.house.airbnbb9.service.AnnouncementVendorService;
import peaksoft.house.airbnbb9.service.BookingService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vendor")
@Tag(name = "VendorAnnouncements Api", description = "Only available for user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnnouncementVendorApi {

    private final AnnouncementVendorService announcementVendorService;
    private final AnnouncementService announcementService;
    private final BookingService bookingService;

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/submitAnAd")
    @Operation(summary = "Submit announcement",
            description = "Submit announcement")
    public SimpleResponse submitAnAd(@RequestBody AnnouncementRequest announcementRequest) {
        return announcementVendorService.submitAnAd(announcementRequest);
    }

    @Operation(summary = "Filter and sort announcements (Vendor part)",
            description = " Filter  announcements by region,house type,rating and price (Vendor part)")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/announcements-filter")
    public List<AnnouncementResponse> getAllAnnouncementsFilterAndSort(
            @RequestParam(required = false) Region region,
            @RequestParam(required = false) HouseType houseType,
            @RequestParam(required = false) String rating,
            @RequestParam(required = false) String price) {
        return announcementService.getAllAnnouncementsFilterVendor(region, houseType, rating, price);
    }

    @Operation(summary = "Global search",
            description = "You can search announcements by region,city,house and apartment")
    @GetMapping("/global-search")
    public GlobalSearchResponse search(@RequestParam String word, @RequestParam(required = false) boolean isNearby ,
                                       @RequestParam(required = false) Double latitude,@RequestParam(required = false) Double longitude) {
        return announcementService.search(word, isNearby,latitude , longitude );
    }

    @Operation(summary = "Request to book",
            description = "Any registered user can submit a booking request.")
    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public Map<String, String> sendRequestToBook(@RequestBody BookRequest request) {
        return bookingService.requestToBook(request);
    }

    @Operation(summary = "Change the date",
            description = "The user can change the booking date.")
    @PreAuthorize("hasAnyAuthority('USER')")
    @PutMapping
    public Map<String, String> updateRequestToBook(@RequestBody UpdateBookRequest request) {
        return bookingService.updateRequestToBook(request);
    }
    @Operation(summary = "Get announcement by id",description = "Get announcement by id into two position as request to book or update booking date")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getAnnouncement/{announcementId}")
    public GetAnnouncementResponse getAnnouncementGetById(@PathVariable Long announcementId){
        return announcementVendorService.getAnnouncementById(announcementId);
    }
}