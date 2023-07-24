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
    @Operation(summary = "GetUserById",description = "Get announcements user by id ")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getById/{id}")
    public List<AnnouncementResponse> getByIdAnnouncement(@PathVariable Long id) {
        return announcementService.getByIdUser(id);
    }
    @Operation(summary = "getAnnouncementByID",description = "Get announcement by id ")
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/{id}")
    public AllAnnouncementResponse getAnnouncementById(@PathVariable Long id) {
        return announcementService.getByIdAnnouncement(id);
    }
    @Operation(summary = "getAnnouncements",description = "Get all announcements")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAll")
    public List<AnnouncementResponse> getAllAnnouncement() {
        return announcementService.getAllAnnouncements();
    }

    @Operation(summary = "update By id",description = "Update announcement by id ")
    @PermitAll
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AnnouncementResponse update(@PathVariable Long id, @RequestBody @Valid AnnouncementRequest announcementRequest) {
        return announcementService.updateAnnouncement(id, announcementRequest);
    }
    @Operation(summary = "delete Announcement By id",description = "Delete announcement by id ")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public SimpleResponse deleteByIdAnnouncement(@PathVariable Long id) {
        return announcementService.deleteByIdAnnouncement(id);
    }
}
