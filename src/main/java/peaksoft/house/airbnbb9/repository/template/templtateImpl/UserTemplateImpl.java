package peaksoft.house.airbnbb9.repository.template.templtateImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.dto.response.UserResponse;
import peaksoft.house.airbnbb9.repository.template.UserTemplate;

import java.util.List;
@Repository
@Transactional
@RequiredArgsConstructor
public class UserTemplateImpl implements UserTemplate {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<UserResponse> getAllUsers() {
        String sql = "SELECT u.id, u.full_name, u.email,\n" +
                "                     COUNT(DISTINCT b.id) as bookings,\n" +
                "                     COUNT(DISTINCT a.id) as announcements\n" +
                "                     FROM Users u\n" +
                "                     LEFT JOIN bookings b ON u.id = b.user_id\n" +
                "                     LEFT JOIN announcements a ON u.id = a.user_id\n" +
                "                     GROUP BY u.id, u.full_name";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserResponse user = new UserResponse();
            user.setId(rs.getLong("id"));
            user.setFullName(rs.getString("full_name"));
            user.setEmail(rs.getString("email"));
            user.setBookings(rs.getInt("bookings"));
            user.setAnnouncements(rs.getInt("announcements"));
            return user;
        });
    }
}
