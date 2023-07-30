package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.response.*;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;

import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

public interface AnnouncementService {
    List<AnnouncementResponse> getAllAnnouncements();
    AnnouncementResponse updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest);
    SimpleResponse deleteByIdAnnouncement(Long announcementId);
    List<AnnouncementResponse> getAllAnnouncementsFilterByStatus(Status status);
    List<AnnouncementResponse> getAllAnnouncementsThePopular(String popular);
    List<AnnouncementResponse> getAllAnnouncementsTheLasted();
    List<AnnouncementResponse> getAllAnnouncementsFilterByHomeType(HouseType houseType);
    List<AnnouncementResponse> getAllAnnouncementsFilterByPriceHighToLow(String highToLow );
    List<AnnouncementResponse> getAllAnnouncementsFilterByPriceLowToHigh();
    List<BookingResponse> getAllAnnouncementsBookings(Long userId);
    List<AnnouncementResponse> getAllMyAnnouncements(Long userId);
    List<PaginationBookingResponse> getAllAnnouncementsBookingsSortAndPagination(String ascOrDesc, int currentPage, int pageSize);
    List<PaginationAnnouncementResponse> getAllMyAnnouncementsSortAndPagination(String ascOrDesc, int currentPage, int pageSize);
}
