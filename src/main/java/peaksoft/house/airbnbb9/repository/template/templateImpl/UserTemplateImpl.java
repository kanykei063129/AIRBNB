package peaksoft.house.airbnbb9.repository.template.templateImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.BookingResponse;
import peaksoft.house.airbnbb9.dto.response.UserResponse;
import peaksoft.house.airbnbb9.repository.template.UserTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
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
                "                     FROM users u\n" +
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
    @Override
    public UserResponse getByIdUser(Long userId,String values) {

        String sql = """
            SELECT u.id as id,u.full_name as fullName, u.email as email
            FROM users u
                     LEFT JOIN bookings b ON u.id = b.user_id
                     LEFT JOIN announcements a ON u.id = a.user_id
            where u.id = ?
            GROUP BY u.id, u.full_name,u.email
            """;

        UserResponse userResponse = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> UserResponse.builder()
                .id(rs.getLong("id"))
                .fullName(rs.getString("fullName"))
                .email(rs.getString("email"))
                .build(), userId);

        if ("booking".equalsIgnoreCase(values)) {
            List<BookingResponse> bookingResponses = getBookingAnnouncementsByUserId(userId);
            assert userResponse != null;
            userResponse.setBookingUser(bookingResponses);
        } else if ("announcement".equalsIgnoreCase(values)) {
            List<AnnouncementResponse> announcementResponses = getAnnouncementByUserId(userId);
            assert userResponse != null;
            userResponse.setAnnouncementResponses(announcementResponses);
        }

        return userResponse;
    }
    private List<AnnouncementResponse> getAnnouncementByUserId(Long userID){
        String sql = """
                SELECT a.id            as id,
                       a.price         as price,
                       a.max_guests    as max_guests,
                       a.address       as address,
                       a.description   as description,
                       a.province      as province,
                       a.region        as region,
                       a.title         as title,
                       AVG(r.rating)   as rating,
                       (SELECT ai.images FROM announcement_images ai
                        WHERE ai.announcement_id = a.id LIMIT 1) as images
                FROM announcements a
                         LEFT JOIN feedbacks r ON a.id = r.announcement_id
                         join users u on u.id = a.user_id
                         WHERE u.id = ?
                GROUP BY a.id, a.price, a.max_guests, a.address,
                         a.description, a.province, a.region, a.title

                 """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> AnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .province(rs.getString("province"))
                .title(rs.getString("title"))
                .images(Collections.singletonList(rs.getString("images")))
                .rating(rs.getInt("rating"))
                .build(),userID);
    }
    private BookingResponse createBookingResponse(ResultSet rs, int rowNum) throws SQLException {
        long bookingId = rs.getLong("id");
        Long userId = rs.getLong("userId");

        List<AnnouncementResponse> announcements = getAnnouncementByUserId(userId);

        return BookingResponse.builder()
                .bookingId(bookingId)
                .announcementResponse(announcements)
                .build();
    }
    private List<BookingResponse> getBookingAnnouncementsByUserId(Long userId) {
        String sql = """
                SELECT b.id as id, b.user_id as userId
                FROM bookings as b
                join users u on u.id = b.user_id
                where u.id = ?;
                """;
        return jdbcTemplate.query(sql, this::createBookingResponse, userId);
    }

}