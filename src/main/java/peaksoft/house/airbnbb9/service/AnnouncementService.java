package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.responce.AnnouncementResponse;
import peaksoft.house.airbnbb9.enums.Status;


import java.util.List;

public interface AnnouncementService {
    List<AnnouncementResponse> getAllAnnouncementsFilterBy(Status status);
    List<AnnouncementResponse> getAllAnnouncementsThePopular(String popular);
    List<AnnouncementResponse> getAllAnnouncementsTheLasted();
    List<AnnouncementResponse> getAllAnnouncementsFilterByHomeType();
    List<AnnouncementResponse> getAllAnnouncementsFilterByPrice();
}
