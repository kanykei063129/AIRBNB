package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.response.UserResponse;
import peaksoft.house.airbnbb9.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
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


