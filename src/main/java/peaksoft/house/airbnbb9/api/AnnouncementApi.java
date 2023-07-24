package peaksoft.house.airbnbb9.api;

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
public class AnnouncementApi {
    private final AnnouncementService announcementService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getById/{id}")
    public List<AnnouncementResponse> getByIdAnnouncement(@PathVariable Long id) {
        return announcementService.getByIdUser(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/{id}")
    public AllAnnouncementResponse getAnnouncementById(@PathVariable Long id) {
        return announcementService.getByIdAnnouncement(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAll")
    public List<AnnouncementResponse> getAllAnnouncement() {
        return announcementService.getAllAnnouncements();
    }

    @PermitAll
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AnnouncementResponse update(@PathVariable Long id, @RequestBody @Valid AnnouncementRequest announcementRequest) {
        return announcementService.updateAnnouncement(id, announcementRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public SimpleResponse deleteByIdAnnouncement(@PathVariable Long id) {
        return announcementService.deleteByIdAnnouncement(id);
    }
}
