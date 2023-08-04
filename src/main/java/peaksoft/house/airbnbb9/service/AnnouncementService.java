package peaksoft.house.airbnbb9.service;


import peaksoft.house.airbnbb9.dto.response.*;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;

import peaksoft.house.airbnbb9.dto.response.BookingResponse;
import peaksoft.house.airbnbb9.dto.response.PaginationAnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.PaginationBookingResponse;

import peaksoft.house.airbnbb9.dto.response.AllAnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;

import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

public interface AnnouncementService {
    List<AnnouncementResponse> getAllAnnouncements();
    AllAnnouncementResponse getByIdAnnouncement(Long announcementId);
    AnnouncementResponse updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest);
    SimpleResponse deleteByIdAnnouncement(Long announcementId);

    List<AnnouncementResponse> getAllAnnouncementsFilterAndSort(Status status, HouseType houseType, boolean ascDesc, boolean lowToHigh);
    List<BookingResponse> getAllAnnouncementsBookings(Long userId);
    List<AnnouncementResponse> getAllMyAnnouncements(Long userId);
    List<PaginationBookingResponse> getAllAnnouncementsBookingsSortAndPagination(String ascOrDesc, int currentPage, int pageSize);
    List<PaginationAnnouncementResponse> getAllMyAnnouncementsSortAndPagination(String ascOrDesc, int currentPage, int pageSize);
}
