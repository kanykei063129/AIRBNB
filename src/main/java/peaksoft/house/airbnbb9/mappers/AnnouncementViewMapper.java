package peaksoft.house.airbnbb9.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnnouncementViewMapper {

    private final JdbcTemplate jdbcTemplate;

    public Double calculateRating() {
        String sql = """
                 SELECT
                     AVG(a.rating) AS rating
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
                     AVG(a.rating) AS rating
                 FROM users u
                          JOIN announcements a ON u.id = a.user_id
                          JOIN feedbacks f ON a.id = f.announcement_id
                 WHERE a.status = 'NOT_BOOKED'
                 """;
        return jdbcTemplate.queryForObject(sql, Double.class);
    }
}
