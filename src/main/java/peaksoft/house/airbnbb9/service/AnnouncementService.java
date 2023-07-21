package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.responce.AnnouncementResponse;
import peaksoft.house.airbnbb9.entity.Announcement;

import java.util.List;

public interface AnnouncementService {
    List<AnnouncementResponse> getAllAnnouncementsFilterBy();
    List<AnnouncementResponse> getAllAnnouncementsSortBy();
    List<AnnouncementResponse> getAllAnnouncementsFilterByHomeType();
    List<AnnouncementResponse> getAllAnnouncementsFilterByPrice();
}
