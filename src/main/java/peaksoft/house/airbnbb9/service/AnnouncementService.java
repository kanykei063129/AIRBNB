package peaksoft.house.airbnbb9.service;

import jakarta.mail.MessagingException;
import peaksoft.house.airbnbb9.dto.response.*;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.PriceType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

public interface AnnouncementService {

    List<AnnouncementResponse> getAllAnnouncements();

    SimpleResponse updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest);

    SimpleResponse deleteByIdAnnouncement(Long announcementId);

    List<AnnouncementResponse> getAllAnnouncementsFilter(Status status, HouseType houseType, String rating, String price);

    List<AnnouncementResponse> getAllAnnouncementsFilterVendor(Region region, HouseType houseType, String rating, String price);

    PaginationAnnouncementResponse getAllAnnouncementsModerationAndPagination(int currentPage, int pageSize);

    LatestAnnouncementResponse getLatestAnnouncement();

    List<PopularHouseResponse> getPopularHouses();

    PopularApartmentResponse getPopularApartment();

    GlobalSearchResponse search(String word, boolean isNearby, Double latitude, Double longitude);

    SimpleResponse processAnnouncement(Long announcementId, String message, String messageFromAdminToUser) throws MessagingException;

    AnnouncementResponse getApplicationById(Long applicationId);

    List<AnnouncementResponse> getAllAnnouncementsFilters(HouseType houseType, String rating, PriceType price);

    PaginationAnnouncementResponse pagination(Integer page, Integer size);

    AnnouncementsResponseProfile getAnnouncementsByIdProfile(Long announcementId);

    SimpleResponse blockedAnnouncementById(Long announcementId);

}