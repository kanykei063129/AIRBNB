package peaksoft.house.airbnbb9.repository.template;

import peaksoft.house.airbnbb9.dto.response.*;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.GlobalSearchResponse;
import peaksoft.house.airbnbb9.dto.response.PaginationAnnouncementResponse;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;
import java.util.List;

public interface AnnouncementTemplate {

    List<AnnouncementResponse> getAllAnnouncementsFilter(Status status, HouseType houseType, String rating, String price);

    List<AnnouncementResponse> getAllAnnouncementsFilterVendor(Region region, HouseType houseType, String rating, String price);

    List<AnnouncementResponse> getAllAnnouncements();

    PaginationAnnouncementResponse getAllAnnouncementsModerationAndPagination(int currentPage, int pageSize);

    LatestAnnouncementResponse getLatestAnnouncement();

    List<PopularHouseResponse> getPopularHouses();

    PopularApartmentResponse getPopularApartment();

    GlobalSearchResponse search(String word);
  
    List<AnnouncementResponse> getAllAnnouncementsFilters(HouseType houseType, String rating, String price);

    PaginationAnnouncementResponse pagination(Integer page, Integer size);
}
