package peaksoft.house.airbnbb9.mappers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.entity.Announcement;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnnouncementViewMapper {

    private final JdbcTemplate jdbcTemplate;

    public Double calculateRating() {
        String sql = """
        SELECT
            AVG(f.rating) AS rating
        FROM users u
        JOIN announcements a ON u.id = a.user_id
        JOIN feedbacks f ON a.id = f.announcement_id
        WHERE a.position =
                'ACCEPTED' OR a.status = 'BOOKED'
        """;

        log.info("Calculating average rating for announcements");

        Double averageRating = jdbcTemplate.queryForObject(sql, Double.class);

        log.info("Calculated average rating successfully: " + averageRating);

        return averageRating;
        }

    public Double calculateRating2() {
        String sql = """
                 SELECT
                     AVG(f.rating) AS rating
                 FROM users u
                          JOIN announcements a ON u.id = a.user_id
                          JOIN feedbacks f ON a.id = f.announcement_id
                 WHERE a.position = 'MODERATION'
                 """;

        log.info("Calculating average rating for announcements with status 'MODERATION'");

        Double averageRating = jdbcTemplate.queryForObject(sql, Double.class);

        log.info("Calculated average rating successfully: " + averageRating);

        return averageRating;
    }

    private void dtoToEntityConverting(AnnouncementRequest request, Announcement announcement) {
        announcement.setTitle(request.getTitle());
        announcement.setDescription(request.getDescription());
        announcement.setImages(request.getImages());
        announcement.setStatus(announcement.getStatus());
        announcement.setPrice(request.getPrice());
        announcement.setMaxGuests(request.getMaxGuests());
        announcement.setHouseType(request.getHouseType());
        announcement.setCreateDate(LocalDate.now());
        announcement.setRegion(request.getRegion());
        announcement.setAddress(request.getAddress());
        announcement.setProvince(request.getProvince());
    }
    public void updateAnnouncement(Announcement announcement, AnnouncementRequest request) {
        dtoToEntityConverting(request, announcement);
    }
}