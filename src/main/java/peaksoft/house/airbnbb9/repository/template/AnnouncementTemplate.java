package peaksoft.house.airbnbb9.repository.template;

import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Status;

import java.util.List;
public interface AnnouncementTemplate {

    List<AnnouncementResponse> getAllAnnouncementsFilterAndSort(Status status, HouseType houseType, boolean ascDesc, boolean lowToHigh);
    List<AnnouncementResponse> getAllAnnouncements();
}
