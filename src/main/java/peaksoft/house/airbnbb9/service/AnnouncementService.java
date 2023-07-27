package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.SimpleResponse;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.AllAnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;

import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

public interface AnnouncementService {
    List<AnnouncementResponse> getAllAnnouncements();
    AllAnnouncementResponse getByIdAnnouncement(Long announcementId);
    AnnouncementResponse updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest);
    SimpleResponse deleteByIdAnnouncement(Long announcementId);
    List<AnnouncementResponse> getAllAnnouncementsFilterByStatus(Status status);
    List<AnnouncementResponse> getAllAnnouncementsThePopular(String popular);
    List<AnnouncementResponse> getAllAnnouncementsTheLasted();
    List<AnnouncementResponse> getAllAnnouncementsFilterByHomeType(HouseType houseType);
    List<AnnouncementResponse> getAllAnnouncementsFilterByPriceHighToLow(String highToLow );
    List<AnnouncementResponse> getAllAnnouncementsFilterByPriceLowToHigh();
}
