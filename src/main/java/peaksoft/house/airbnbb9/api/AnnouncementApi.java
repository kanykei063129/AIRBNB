package peaksoft.house.airbnbb9.api;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.SimpleResponse;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.service.AnnouncementService;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementApi {
    private final AnnouncementService announcementService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/getById/{id}")
    public List<AnnouncementResponse> getByIdAnnouncement(@PathVariable Long userId) {
        return announcementService.getByIdUser(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public List<AnnouncementResponse> getAllAnnouncement() {
        return announcementService.getAllAnnouncements();
    }

    @PermitAll
    @PutMapping("/{id}")

    public AnnouncementResponse update(@PathVariable Long announcementId, @RequestBody @Valid AnnouncementRequest announcementRequest) {
        return announcementService.updateAnnouncement(announcementId, announcementRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public SimpleResponse deleteByIdAnnouncement(@PathVariable Long id) {
        return announcementService.deleteByIdAnnouncement(id);
    }
}
