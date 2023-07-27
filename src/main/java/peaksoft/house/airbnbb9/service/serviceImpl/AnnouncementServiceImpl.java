package peaksoft.house.airbnbb9.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.responce.AnnouncementResponse;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.service.AnnouncementService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilterByStatus(Status status) {
        String sql = "SELECT a.id, a.price, a.max_guests, a.address, a.title, ai.images, a.status " +
                "FROM announcements a " +
                "LEFT JOIN announcement_images ai ON a.id = ai.announcement_id " +
                "WHERE a.status = ?";

        return jdbcTemplate.query(sql, new Object[]{status.name()}, (rs, rowNum) -> new AnnouncementResponse(
                rs.getLong("id"),
                rs.getInt("price"),
                rs.getInt("max_guests"),
                rs.getString("address"),
                rs.getString("title"),
                Collections.singletonList(rs.getString("images")),
                Status.valueOf(rs.getString("status"))
        ));
    }


    @Override
    public List<AnnouncementResponse> getAllAnnouncementsThePopular(String popular) {
        String sql = "SELECT * FROM announcements a\n" +
                "                LEFT JOIN announcement_images ai ON a.id = ai.announcement_id\n" +
                "                 JOIN feedbacks r ON a.id = r.announcement_id\n" +
                "                ORDER BY r.rating DESC LIMIT 2";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new AnnouncementResponse(
                rs.getLong("id"),
                rs.getInt("price"),
                rs.getInt("max_guests"),
                rs.getString("address"),
                rs.getString("title"),
                Collections.singletonList(rs.getString("images")),
                Status.valueOf(rs.getString("status")),
                rs.getInt("rating")
        ));
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsTheLasted() {
        String sql = "SELECT * FROM announcements a\n" +
                "                LEFT JOIN announcement_images ai ON a.id = ai.announcement_id\n" +
                "                 JOIN feedbacks r ON a.id = r.announcement_id\n" +
                "                ORDER BY r.rating ASC LIMIT 2 ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new AnnouncementResponse(
                rs.getLong("id"),
                rs.getInt("price"),
                rs.getInt("max_guests"),
                rs.getString("address"),
                rs.getString("title"),
                Collections.singletonList(rs.getString("images")),
                Status.valueOf(rs.getString("status")),
                rs.getInt("rating")
        ));
    }


    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilterByHomeType(HouseType houseType) {
        String sql = "SELECT a.id, a.price, a.max_guests, a.address, a.title, ai.images, a.status\n" +
                "FROM announcements a " +
                "LEFT JOIN announcement_images ai ON a.id = ai.announcement_id WHERE a.house_type = ?";

        return jdbcTemplate.query(sql, new Object[]{houseType.name()}, (rs, rowNum) -> new AnnouncementResponse(
                rs.getLong("id"),
                rs.getInt("price"),
                rs.getInt("max_guests"),
                rs.getString("address"),
                rs.getString("title"),
                Collections.singletonList(rs.getString("images")),
                Status.valueOf(rs.getString("status"))
        ));
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilterByPriceHighToLow(String highToLow) {
        String sql = "SELECT * FROM announcements a LEFT JOIN announcement_images ai ON a.id = ai.announcement_id\n" +
                "JOIN feedbacks r ON a.id = r.announcement_id\n" +
                "ORDER BY a.price DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new AnnouncementResponse(
                rs.getLong("id"),
                rs.getInt("price"),
                rs.getInt("max_guests"),
                rs.getString("address"),
                rs.getString("title"),
                Collections.singletonList(rs.getString("images")),
                Status.valueOf(rs.getString("status")),
                rs.getInt("rating")
        ));
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilterByPriceLowToHigh() {
        String sql = "SELECT * FROM announcements a LEFT JOIN announcement_images ai ON a.id = ai.announcement_id\n" +
                "JOIN feedbacks r ON a.id = r.announcement_id\n" +
                "ORDER BY a.price ASC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new AnnouncementResponse(
                rs.getLong("id"),
                rs.getInt("price"),
                rs.getInt("max_guests"),
                rs.getString("address"),
                rs.getString("title"),
                Collections.singletonList(rs.getString("images")),
                Status.valueOf(rs.getString("status")),
                rs.getInt("rating")
        ));
    }
}
