package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.AnnouncementsResponseProfile;
import peaksoft.house.airbnbb9.dto.response.GetAnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;

public interface AnnouncementVendorService {
    SimpleResponse submitAnAd(AnnouncementRequest announcementRequest);
    GetAnnouncementResponse getAnnouncementById(Long announcementId);
}
