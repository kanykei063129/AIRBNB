package peaksoft.house.airbnbb9.repository.template.templateImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.dto.response.*;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.PriceType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.exception.BadCredentialException;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.repository.template.UserTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserTemplateImpl implements UserTemplate {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        String sql = "SELECT u.id, u.full_name, u.email,\n" +
                "                     COUNT(DISTINCT b.id) as bookings,\n" +
                "                     COUNT(DISTINCT a.id) as announcements\n" +
                "                     FROM users u\n" +
                "                     LEFT JOIN bookings b ON u.id = b.user_id\n" +
                "                     LEFT JOIN announcements a ON u.id = a.user_id\n" +
                "                     GROUP BY u.id, u.full_name";

        log.info("Fetching all users with booking and announcement counts.");

        List<UserResponse> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserResponse user = new UserResponse();
            user.setId(rs.getLong("id"));
            user.setFullName(rs.getString("full_name"));
            user.setEmail(rs.getString("email"));
            user.setBookings(rs.getInt("bookings"));
            user.setAnnouncements(rs.getInt("announcements"));
            return user;
        });

        log.info("Fetched all users with booking and announcement counts successfully!");
        return users;
    }

    @Override
    public UserResponse getByIdUser(Long userId, String values) {

        String sql = """
                SELECT u.id as id,u.full_name as fullName, u.email as email
                FROM users u
                         LEFT JOIN bookings b ON u.id = b.user_id
                         LEFT JOIN announcements a ON u.id = a.user_id
                where u.id = ?
                GROUP BY u.id, u.full_name,u.email
                """;

        log.info("Fetching user by ID: " + userId + " with additional values: " + values);

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
            List<AnnouncementResponseUser> announcementResponses = getAnnouncementByUserId(userId);
            assert userResponse != null;
            userResponse.setAnnouncementResponses(announcementResponses);
        }
        log.info("Fetched user by ID: " + userId + " successfully with additional values: " + values);
        return userResponse;
    }
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        log.info("Getting authenticated user with email: {}", login);
        return userRepository.getUserByEmail(login).orElseThrow(() ->
                new BadCredentialException("An unregistered user cannot write comment for this announcement!"));
    }
    @Override
    public FilterResponse getAllAnnouncementsFilters(HouseType houseType, String rating, PriceType price) {
        User user = getAuthenticatedUser();
        StringBuilder sql = new StringBuilder("SELECT a.id, a.price, a.max_guests, a.address, a.description, a.province, a.region, a.house_type, a.status, a.title, COALESCE(r.rating, 0) as rating, ");
        sql.append("(SELECT ARRAY_AGG(ai.images) FROM announcement_images ai WHERE ai.announcement_id = a.id) as images ");
        sql.append("FROM announcements a ");
        sql.append("LEFT JOIN feedbacks r ON a.id = r.announcement_id ");
        sql.append("WHERE a.user_id = ? AND a.position = 'ACCEPTED'");
        List<Object> params = new ArrayList<>();
        params.add(user.getId());

        if (houseType != null) {
            sql.append("AND a.house_type = ? ");
            params.add(houseType.name());
        }

        if (rating != null && !rating.isEmpty()) {
            sql.append("AND r.rating IS NOT NULL ");
        }

        if (price != null) {
            sql.append("AND a.price IS NOT NULL ");
        }

        sql.append("GROUP BY a.id, a.price, a.max_guests, a.address, a.description, a.province, a.house_type, a.status, a.region, a.title, rating, images ");

        if (rating != null && !rating.isEmpty()) {
            sql.append("ORDER BY rating " + (rating.equalsIgnoreCase("asc") ? "ASC" : "DESC"));
        } else if (price != null && !price.equals(PriceType.LOW_TO_HIGH)) {
            sql.append("ORDER BY a.price DESC");
        } else if (price != null) {
            sql.append("ORDER BY a.price ASC");
        }
        log.info("Fetching announcements with filters: HouseType - " + houseType + ", Rating - " + rating + ", PriceType - " + price);
        List<ProfileAnnouncementResponse> profileAnnouncementResponses = jdbcTemplate.query(sql.toString(), (rs, rowNum) -> ProfileAnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .houseType(HouseType.valueOf(rs.getString("house_type")))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .province(rs.getString("province"))
                .title(rs.getString("title"))
                .rating(rs.getInt("rating"))
                .status(Status.valueOf(rs.getString("status")))
                .region(Region.valueOf(rs.getString("region")))
                .images(Arrays.asList((String[]) rs.getArray("images").getArray())) // Преобразование PgArray в List<String>
                .build(), params.toArray());

        FilterResponse filterResponse = new FilterResponse(profileAnnouncementResponses);

        log.info("Fetched announcements with filters successfully!");
        return filterResponse;
    }

    private List<AnnouncementResponseUser> getAnnouncementByUserId(Long userID) {
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

        log.info("Fetching announcements by user ID: " + userID);

        List<AnnouncementResponseUser> announcementResponses = jdbcTemplate.query(sql, (rs, rowNum) -> AnnouncementResponseUser.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .province(rs.getString("province"))
                .title(rs.getString("title"))
                .images(Collections.singletonList(rs.getString("images")))
                .rating(rs.getInt("rating"))
                .build(), userID);

        log.info("Fetched announcements by user ID: " + userID + " successfully!");
        return announcementResponses;
    }

    private List<BookingResponse> getBookingAnnouncementsByUserId(Long userId) {
        String sql = """
                SELECT a.id as id, a.price as price, a.max_guests as max_guests, a.address as address,
                    a.description as description, a.province as province, a.region as region, a.title as title,
                    AVG(r.rating) as rating,
                    (SELECT ai.images FROM announcement_images ai WHERE ai.announcement_id = a.id LIMIT 1) as images
                FROM announcements a
                LEFT JOIN feedbacks r ON a.id = r.announcement_id
                JOIN bookings b ON a.id = b.announcement_id 
                WHERE b.user_id = ?
                GROUP BY a.id, a.price, a.max_guests, a.address,
                    a.description, a.province, a.region, a.title
                                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> BookingResponse.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .province(rs.getString("province"))
                .title(rs.getString("title"))
                .images(Collections.singletonList(rs.getString("images")))
                .rating(rs.getInt("rating"))
                .build(), userId);

    }
}