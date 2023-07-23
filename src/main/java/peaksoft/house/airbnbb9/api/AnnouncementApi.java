package peaksoft.house.airbnbb9.api;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.responce.AnnouncementResponse;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.service.AnnouncementService;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementApi {
    private final AnnouncementService announcementService;

    @GetMapping("/filter")
    public List<AnnouncementResponse> getAllAnnouncementsFilterByStatus(@RequestParam(name = "FilterByStatus") Status status) {
        if (status == null) {
            return null;
        } else {
            return announcementService.getAllAnnouncementsFilterBy(status);
        }
    }

    @GetMapping("sort")
    public List<AnnouncementResponse> getAllAnnouncementsSortByRating(@RequestParam(name = "sortByRating") String popular) {
        if (popular == null) {
            return null;
        } else if (popular.equals("popular")) {
            return announcementService.getAllAnnouncementsThePopular(popular);
        } else
            return announcementService.getAllAnnouncementsTheLasted();
    }
}
