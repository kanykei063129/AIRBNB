package peaksoft.house.airbnbb9.repository.template.templtateImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.PaginationAnnouncementResponse;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.repository.template.AnnouncementTemplate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class AnnouncementTemplateImpl implements AnnouncementTemplate {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilter(Status status, HouseType houseType) {
        String sql = """
                SELECT a.id            as id,
                       a.price         as price,
                       a.max_guests    as max_guests,
                       a.address       as address,
                       a.description   as description,
                       a.province      as province,
                       a.region        as region,
                       a.title         as title,
                       r.rating        as rating,
                       (SELECT ai.images FROM announcement_images ai WHERE ai.announcement_id = a.id LIMIT 1) as images
                FROM announcements a
                        LEFT JOIN feedbacks r ON a.id = r.announcement_id
                WHERE 1=1
                """;

        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql += " AND a.status = ?";
            params.add(status.name());
        }

        if (houseType != null) {
            sql += " AND a.house_type = ?";
            params.add(houseType.name());
        }

        sql += " GROUP BY a.id, a.price, a.max_guests, a.address, a.description, a.province, a.region, a.title,r.rating";

        return jdbcTemplate.query(sql, params.toArray(), (rs, rowNum) -> AnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .province(rs.getString("province"))
                .title(rs.getString("title"))
                .images(Collections.singletonList(rs.getString("images")))
                .rating(rs.getInt("rating"))
                .build());
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsSort(String rating, String price) {
        String sql = """
                        SELECT a.id            as id,
                               a.price         as price,
                               a.max_guests    as max_guests,
                               a.address       as address,
                               a.description   as description,
                               a.province      as province,
                               a.region        as region,
                               a.title         as title,
                               r.rating        as rating,
                               (SELECT ai.images FROM announcement_images ai WHERE ai.announcement_id = a.id LIMIT 1) as images
                        FROM announcements a
                                LEFT JOIN feedbacks r ON a.id = r.announcement_id
                        GROUP BY a.id, a.price, a.max_guests, a.address,
                                 a.description, a.province, a.region, a.title, r.rating
                """;

        if ("asc".equalsIgnoreCase(rating)) {
            sql += " ORDER BY rating ASC";
        } else if ("desc".equalsIgnoreCase(rating)) {
            sql += " ORDER BY rating DESC";
        }

        if ("asc".equalsIgnoreCase(price)) {
            sql += " ORDER BY price ASC";
        } else if ("desc".equalsIgnoreCase(price)) {
            sql += " ORDER BY price DESC";
        }

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
                .build());
    }


    @Override
    public List<AnnouncementResponse> getAllAnnouncements() {
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
                       (SELECT ai.images FROM announcement_images ai WHERE ai.announcement_id = a.id LIMIT 1) as images
                FROM announcements a
                         LEFT JOIN feedbacks r ON a.id = r.announcement_id
                         WHERE a.status = 'MODERATION'
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
                .build());
    }

    @Override
    public PaginationAnnouncementResponse getAllAnnouncementsModerationAndPagination(int currentPage, int pageSize) {
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
                       (SELECT ai.images FROM announcement_images ai WHERE ai.announcement_id = a.id LIMIT 1) as images
                FROM announcements a
                         LEFT JOIN feedbacks r ON a.id = r.announcement_id
                         WHERE a.status = 'MODERATION'
                GROUP BY a.id, a.price, a.max_guests, a.address,
                         a.description, a.province, a.region, a.title,a.create_date
                order by a.create_date 
                 """;
        int offset = (currentPage - 1) * pageSize;
        sql+= "LIMIT ? OFFSET ?";

        List<AnnouncementResponse> announcementResponses = jdbcTemplate.query(sql, (rs, rowNum) -> AnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .province(rs.getString("province"))
                .title(rs.getString("title"))
                .images(Collections.singletonList(rs.getString("images")))
                .rating(rs.getInt("rating"))
                .build(),pageSize,offset);

        return new PaginationAnnouncementResponse(announcementResponses,currentPage,pageSize);
    }
}
