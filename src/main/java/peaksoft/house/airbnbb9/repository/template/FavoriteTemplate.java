package peaksoft.house.airbnbb9.repository.template;
import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.dto.response.FavoriteAnnouncementsResponse;
import java.util.List;
@Repository
public interface FavoriteTemplate {

    List<FavoriteAnnouncementsResponse> getAllFavoriteAnnouncements();

}
