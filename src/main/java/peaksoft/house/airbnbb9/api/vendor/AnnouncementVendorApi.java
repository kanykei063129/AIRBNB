package peaksoft.house.airbnbb9.api.vendor;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.service.AnnouncementVendorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vendor")
@Tag(name = "VendorAnnouncements Api", description = "user send ad to admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnnouncementVendorApi {
    private final AnnouncementVendorService announcementVendorService;

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/submitAnAd")
    public SimpleResponse submitAnAd(@RequestBody AnnouncementRequest announcementRequest) {
        return announcementVendorService.submitAnAd(announcementRequest);
    }

}

