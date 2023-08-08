package peaksoft.house.airbnbb9.repository.template;

import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.PaginationAnnouncementResponse;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;

public interface AnnouncementTemplate {

    List<AnnouncementResponse> getAllAnnouncementsFilter(Status status, HouseType houseType,String rating, String price);

    List<AnnouncementResponse> getAllAnnouncements();

    PaginationAnnouncementResponse getAllAnnouncementsModerationAndPagination(int currentPage, int pageSize);
}
