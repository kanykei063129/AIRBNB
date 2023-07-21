package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.SimpleResponse;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;

import java.util.List;

public interface AnnouncementService {
    List<AnnouncementResponse> getAllAnnouncements();
    List<AnnouncementResponse> getByIdUser(Long userId);
    AnnouncementResponse updateAnnouncement(Long announcementId,AnnouncementRequest announcementRequest);
    SimpleResponse deleteByIdAnnouncement(Long id);
}
