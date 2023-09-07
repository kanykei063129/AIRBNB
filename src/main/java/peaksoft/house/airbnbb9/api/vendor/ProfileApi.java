package peaksoft.house.airbnbb9.api.vendor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.AnnouncementsResponseProfile;
import peaksoft.house.airbnbb9.dto.response.UserProfileResponse;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.PriceType;
import peaksoft.house.airbnbb9.repository.template.AnnouncementTemplate;
import peaksoft.house.airbnbb9.service.AnnouncementService;
import peaksoft.house.airbnbb9.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "ProfileApi", description = "Any registered user can see their profile")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProfileApi {

    private final UserService userService;
    private final AnnouncementService announcementService;

    @Operation(summary = "User profile",
            description = "Any registered user can access their own profile")
    @GetMapping("bookings/my-announcements")
    public UserProfileResponse getUserBookingsAndAnnouncements() {
        return userService.getUserProfile();
    }

    @Operation(summary = "Any registered user can filter announcements in the profile",
            description = "Filter accepted announcements by popular,house type, and price low to high and high to low")
    @GetMapping("/filter")
    public List<AnnouncementResponse> getAllAnnouncementsFilters(
            @RequestParam(required = false) HouseType houseType,
            @RequestParam(required = false) String rating,
            @RequestParam(required = false) PriceType price) {
        return announcementService.getAllAnnouncementsFilters(houseType, rating, price);
    }

    @Operation(summary = "Get announcement by id", description = "Get announcement by id into two position as request to book or update booking date")
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/getAnnouncementProfile/{announcementId}")
    public AnnouncementsResponseProfile getAnnouncementByIdProfile(@PathVariable Long announcementId) {
        return announcementService.getAnnouncementsByIdProfile(announcementId);
    }
}
