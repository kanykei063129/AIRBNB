package peaksoft.house.airbnbb9.service;
import peaksoft.house.airbnbb9.dto.response.FavoriteAnnouncementsResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import java.util.List;
public interface FavoriteService {
    List<FavoriteAnnouncementsResponse> getAllFavoriteAnnouncements();
    SimpleResponse addOrRemoveFavorite(Long announcementId);
}
