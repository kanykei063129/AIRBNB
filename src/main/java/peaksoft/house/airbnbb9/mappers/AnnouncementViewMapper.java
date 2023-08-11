package peaksoft.house.airbnbb9.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import peaksoft.house.airbnbb9.dto.response.AnnouncementInnerPageResponse;
import peaksoft.house.airbnbb9.entity.Announcement;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnnouncementViewMapper {

    private final JdbcTemplate jdbcTemplate;

    public Double calculateRating() {
        String sql = """
                 SELECT
                     AVG(f.rating) AS rating
                 FROM users u
                          JOIN announcements a ON u.id = a.user_id
                          JOIN feedbacks f ON a.id = f.announcement_id
                 WHERE a.status = 'BOOKED'
                 """;
        return jdbcTemplate.queryForObject(sql, Double.class);
    }

    public Double calculateRating1() {
        String sql = """
                 SELECT
                     AVG(f.rating) AS rating
                 FROM users u
                          JOIN announcements a ON u.id = a.user_id
                          JOIN feedbacks f ON a.id = f.announcement_id
                 WHERE a.status = 'NOT_BOOKED'
                 """;
        return jdbcTemplate.queryForObject(sql, Double.class);
    }
    public AnnouncementInnerPageResponse entityToDtoConverting(Announcement announcement) {
        if (announcement == null) {
            return null;
        }
        AnnouncementInnerPageResponse response = new AnnouncementInnerPageResponse();
        response.setId(announcement.getId());
        response.setImages(announcement.getImages());
        response.setHouseType(announcement.getHouseType());
        response.setMaxGuests(announcement.getMaxGuests());
        response.setAddress(announcement.getAddress());
        response.setDescription(announcement.getDescription());
        response.setPrice(BigDecimal.valueOf(announcement.getPrice()));
        response.setUserID(announcement.getUser().getId());
        response.setUserImage(announcement.getUser().getImage());
        response.setUserFullName(announcement.getUser().getFullName());
        response.setUserEmail(announcement.getUser().getEmail());
        return response;
    }
}