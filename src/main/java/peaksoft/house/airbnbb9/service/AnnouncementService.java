package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.response.*;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

public interface AnnouncementService {
    List<AnnouncementResponse> getAllAnnouncements();

    AllAnnouncementResponse getByIdAnnouncement(Long announcementId);

    AnnouncementResponse updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest);

    SimpleResponse deleteByIdAnnouncement(Long announcementId);

    List<AnnouncementResponse> getAllAnnouncementsFilter(Status status, HouseType houseType,String rating, String price);
    List<AnnouncementResponse> getAllAnnouncementsFilterVendor(Region region, HouseType houseType, String rating, String price);


    List<BookingResponse> getAllAnnouncementsBookings(Long userId);

    List<AnnouncementResponse> getAllMyAnnouncements(Long userId);

    List<PaginationBookingResponse> getAllAnnouncementsBookingsSortAndPagination(String ascOrDesc, int currentPage, int pageSize);

    List<PaginationAnnouncementResponse> getAllMyAnnouncementsSortAndPagination(String ascOrDesc, int currentPage, int pageSize);

    SimpleResponse approveAnnouncement(Long announcementId);

    SimpleResponse rejectAnnouncement(Long announcementId);

    PaginationAnnouncementResponse getAllAnnouncementsModerationAndPagination(int currentPage, int pageSize);

    LastestAnnouncementResponse getLastestAnnouncement ();

    List<PopularHouseResponse> getPopularHouses();

    PopularApartmentResponse getPopularApartment();
}