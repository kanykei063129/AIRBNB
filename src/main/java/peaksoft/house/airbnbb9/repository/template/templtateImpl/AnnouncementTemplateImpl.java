package peaksoft.house.airbnbb9.repository.template.templtateImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.repository.template.AnnouncementTemplate;

import java.util.Collections;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class AnnouncementTemplateImpl implements AnnouncementTemplate {
    private final JdbcTemplate jdbcTemplate;

//    @Override
//    public List<AnnouncementResponse> getAllAnnouncementsFilterAndSort(Status status, HouseType houseType, String sortBy, String sortOrder) {
//        String sql = """
//                SELECT a.id            as id,
//                       a.price         as price,
//                       a.max_guests    as max_guests,
//                       a.address       as address,
//                       a.description   as description,
//                       a.province      as province,
//                       a.region        as region,
//                       a.title         as title,
//                       AVG(r.rating)   as rating,
//                       (SELECT ai.images FROM announcement_images ai WHERE ai.announcement_id = a.id LIMIT 1) as images
//                FROM announcements a
//                         JOIN feedbacks r ON a.id = r.announcement_id
//                WHERE a.status = ?
//                GROUP BY a.id, a.price, a.max_guests, a.address,
//                         a.description, a.province, a.region, a.title
//                """;
//        return jdbcTemplate.query(sql, (rs, rowNum) -> AnnouncementResponse.builder()
//                .id(rs.getLong("id"))
//                .price(rs.getInt("price"))
//                .maxGuests(rs.getInt("max_guests"))
//                .address(rs.getString("address"))
//                .description(rs.getString("description"))
//                .province(rs.getString("province"))
//                .title(rs.getString("title"))
//                .images(Collections.singletonList(rs.getString("images")))
//                .rating(rs.getInt("rating"))
//                .build(),status.name());
//    }
@Override
public List<AnnouncementResponse> getAllAnnouncementsFilterAndSort(
        Status status,
        HouseType houseType,
        boolean ascDesc,
        boolean lowToHigh
) {
    // Проверяем, что status не равен null, прежде чем использовать его в запросе
    if (status == null) {
        throw new IllegalArgumentException("Status cannot be null.");
    }

    String sql = """
            SELECT a.id            as id,
                   a.price         as price,
                   a.max_guests    as max_guests,
                   a.address       as address,
                   a.description   as description,
                   a.province      as province,
                   a.region        as region,
                   a.title         as title,
                   AVG(r.rating) OVER (PARTITION BY a.id) as rating,
                   (SELECT ai.images FROM announcement_images ai WHERE ai.announcement_id = a.id LIMIT 1) as images
            FROM announcements a
                     JOIN feedbacks r ON a.id = r.announcement_id
            WHERE a.status = ?""";

    // Добавляем условия фильтрации по houseType и сортировки по цене, если параметры переданы
    if (houseType != null) {
        sql += " AND a.house_type = ?";
    }

    // Добавляем условия сортировки по цене и рейтингу
    if (lowToHigh) {
        sql += " ORDER BY price ASC";
    } else {
        sql += " ORDER BY price DESC";
    }

    if (ascDesc) {
        sql += ", rating ASC";
    } else {
        sql += ", rating DESC";
    }

    // Возвращаем результаты запроса, передавая параметры в метод query
    return jdbcTemplate.query(sql, (rs, rowNum) -> AnnouncementResponse.builder()
                    .id(rs.getLong("id"))
                    .price(rs.getInt("price"))
                    .maxGuests(rs.getInt("max_guests"))
                    .address(rs.getString("address"))
                    .description(rs.getString("description"))
                    .province(rs.getString("province"))
                    .title(rs.getString("title"))
                    .images(Collections.singletonList(rs.getString("images")))
                    .rating(rs.getDouble("rating"))
                    .build(),
            status.name(),
            (houseType != HouseType.APARTMENT) ? houseType.name() : null // Добавляем значение параметра houseType, если он передан
    );
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
                         JOIN feedbacks r ON a.id = r.announcement_id
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
}
