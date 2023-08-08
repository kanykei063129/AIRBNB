package peaksoft.house.airbnbb9.api.vendor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.service.AnnouncementService;
import peaksoft.house.airbnbb9.service.AnnouncementVendorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vendor")
@Tag(name = "VendorAnnouncements Api", description = "user send ad to admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnnouncementVendorApi {
    private final AnnouncementVendorService announcementVendorService;
    private final AnnouncementService announcementService;

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/submitAnAd")
    public SimpleResponse submitAnAd(@RequestBody AnnouncementRequest announcementRequest) {
        return announcementVendorService.submitAnAd(announcementRequest);
    }

    @Operation(summary = "filter and sort announcements", description = " filter  announcements by region and house type  ")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/announcements-filter")
    public List<AnnouncementResponse> getAllAnnouncementsFilterAndSort(
            @RequestParam(required = false) Region region,
            @RequestParam(required = false) HouseType houseType,
            @RequestParam(required = false) String rating,
            @RequestParam(required = false) String price) {
        return announcementService.getAllAnnouncementsFilterVendor(region, houseType, rating, price);
    }

}

