package peaksoft.house.airbnbb9.api.vendor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.response.FavoriteAnnouncementsResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.service.FavoriteService;
import java.util.List;
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorite Api",description = "All favorites endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FavoriteApi {
    private final FavoriteService favoriteService;
    @Operation(summary = "Get  all user's favorite announcements",description = "Get  all user's favorite announcements")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getAllFavorites")
    public List<FavoriteAnnouncementsResponse> getAllFavorites(){
      return   favoriteService.getAllFavoriteAnnouncements();
    }
    @Operation(summary = "Add or remove Announcement from favorites.",description = "Add or remove Announcements from favorites.")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/{announcementId}")
    public SimpleResponse addOrRemoveFavorite(@PathVariable("announcementId") Long announcementId){
        return favoriteService.addOrRemoveFavorite(announcementId);
    }

}
