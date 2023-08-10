package peaksoft.house.airbnbb9.repository.template.templateImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.dto.response.*;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.GlobalSearchResponse;
import peaksoft.house.airbnbb9.dto.response.PaginationAnnouncementResponse;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.repository.template.AnnouncementTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Repository
@Transactional
@RequiredArgsConstructor
public class AnnouncementTemplateImpl implements AnnouncementTemplate {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilter(Status status, HouseType houseType, String rating, String price) {
        String sql = "SELECT a.id, a.price, a.max_guests, a.address, a.description, a.province, a.region, a.title, r.rating, "
                + "(SELECT ai.images FROM announcement_images ai WHERE ai.announcement_id = a.id LIMIT 1) as images "
                + "FROM announcements a "
                + "LEFT JOIN feedbacks r ON a.id = r.announcement_id "
                + "WHERE 1=1 ";

        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql += "AND a.status = ? ";
            params.add(status.name());
        }

        if (houseType != null) {
            sql += "AND a.house_type = ? ";
            params.add(houseType.name());
        }

        if (rating != null && !rating.isEmpty()) {
            sql += "AND r.rating IS NOT NULL ";
        }

        if (price != null && !price.isEmpty()) {
            sql += "AND a.price IS NOT NULL ";
        }

        sql += "GROUP BY a.id, a.price, a.max_guests, a.address, a.description, a.province, a.region, a.title, r.rating, images ";

        if (rating != null && !rating.isEmpty()) {
            sql += "ORDER BY r.rating " + (rating.equalsIgnoreCase("asc") ? "ASC" : "DESC");
        } else if (price != null && !price.isEmpty()) {
            sql += "ORDER BY a.price " + (price.equalsIgnoreCase("asc") ? "ASC" : "DESC");
        }

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
    public List<AnnouncementResponse> getAllAnnouncementsFilterVendor(Region region, HouseType houseType, String rating, String price) {
        String sql = "SELECT a.id, a.price, a.max_guests, a.address, a.description, a.province, a.region, a.title, r.rating, "
                + "(SELECT ai.images FROM announcement_images ai WHERE ai.announcement_id = a.id LIMIT 1) as images "
                + "FROM announcements a "
                + "LEFT JOIN feedbacks r ON a.id = r.announcement_id "
                + "WHERE 1=1 ";

        List<Object> params = new ArrayList<>();

        if (region != null) {
            sql += "AND a.region = ? ";
            params.add(region.name());
        }

        if (houseType != null) {
            sql += "AND a.house_type = ? ";
            params.add(houseType.name());
        }

        if (rating != null && !rating.isEmpty()) {
            sql += "AND r.rating IS NOT NULL ";
        }

        if (price != null && !price.isEmpty()) {
            sql += "AND a.price IS NOT NULL ";
        }

        sql += "GROUP BY a.id, a.price, a.max_guests, a.address, a.description, a.province, a.region, a.title, r.rating, images ";

        if (rating != null && !rating.isEmpty()) {
            sql += "ORDER BY r.rating " + (rating.equalsIgnoreCase("asc") ? "ASC" : "DESC");
        } else if (price != null && !price.isEmpty()) {
            sql += "ORDER BY a.price " + (price.equalsIgnoreCase("asc") ? "ASC" : "DESC");
        }

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
                       (SELECT ai.images FROM announcement_images ai
                        WHERE ai.announcement_id = a.id LIMIT 1) as images
                FROM announcements a
                         LEFT JOIN feedbacks r ON a.id = r.announcement_id
                         WHERE a.status =?
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
                SELECT a.id            AS id,
                       a.price         AS price,
                       a.max_guests    AS max_guests,
                       a.address       AS address,
                       a.description   AS description,
                       a.province      AS province,
                       a.region        AS region,
                       a.title         AS title,
                       AVG(r.rating)   AS rating,
                       (SELECT ai.images 
                       FROM announcement_images ai 
                       WHERE ai.announcement_id = a.id 
                       LIMIT 1) AS images
                FROM announcements a
                         LEFT JOIN feedbacks r ON a.id = r.announcement_id
                         WHERE a.status = 'MODERATION'
                GROUP BY a.id, a.price, a.max_guests, a.address,
                         a.description, a.province, a.region, a.title,a.create_date
                ORDER BY a.create_date 
                 """;
        int offset = (currentPage - 1) * pageSize;
        sql += "LIMIT ? OFFSET ?";

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
                .build(), pageSize, offset);

        return new PaginationAnnouncementResponse(announcementResponses, currentPage, pageSize);
    }


    @Override
    public LastestAnnouncementResponse getLastestAnnouncement() {

        String sql = """
                SELECT 
                       a.address       AS address,
                       a.description   AS description,
                       a.title         AS title,
                       (SELECT string_agg(ai.images, ',')
                        FROM announcement_images ai
                        WHERE ai.announcement_id = a.id) AS images
                FROM announcements a 
                LEFT JOIN announcement_images i on a.id = i.announcement_id 
                ORDER BY create_date 
                DESC LIMIT 1
                 """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> LastestAnnouncementResponse.builder()
                .images(Arrays.asList(rs.getString("images").split(",")))
                .title(rs.getString("title"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .build());
    }

    @Override
    public List<PopularHouseResponse> getPopularHouses() {

        String sql = """
                SELECT 
                       a.address,       
                       a.description, 
                       a.title,     
                       a.price,
                       AVG(f.rating) AS rating,   
                       (SELECT ai.images 
                       FROM announcement_images ai 
                       WHERE ai.announcement_id = a.id 
                       LIMIT 1) AS image
                FROM announcements a  
                LEFT JOIN feedbacks f 
                ON a.id = f.announcement_id 
                WHERE a.house_type ='HOUSE' 
                GROUP BY     
                a.id,
                a.address,
                a.description,
                a.title,
                a.price 
                ORDER BY rating 
                DESC LIMIT 3;
                 """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> PopularHouseResponse.builder()
                .title(rs.getString("title"))
                .image(rs.getString("image"))
                .address(rs.getString("address"))
                .price(rs.getInt("price"))
                .rating(rs.getDouble("rating"))
                .build());
    }

    @Override
    public PopularApartmentResponse getPopularApartment() {
        String sql = """
                SELECT 
                          a.address,       
                          a.description, 
                          a.title,    
                          AVG(f.rating) AS rating,   
                          (SELECT string_agg(ai.images, ',')
                           FROM announcement_images ai
                           WHERE ai.announcement_id = a.id) AS images
                   FROM announcements a  
                   LEFT JOIN feedbacks f 
                   ON a.id = f.announcement_id  
                   WHERE a.house_type ='APARTMENT'
                   GROUP BY     
                   a.id,
                   a.address,
                   a.description,
                   a.title
                   ORDER BY rating 
                   DESC LIMIT 1;
                    """;
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> PopularApartmentResponse.builder()
                        .images(Arrays.asList(rs.getString("images").split(",")))
                        .title(rs.getString("title"))
                        .address(rs.getString("address"))
                        .description(rs.getString("description"))
                        .build());

    @Override
    public GlobalSearchResponse search(String word) {
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
                WHERE 
                    a.region ILIKE lower(concat('%', ?, '%'))  
                    OR a.status ILIKE lower(concat('%', ?, '%'))
                    OR a.house_type ILIKE lower(concat('%',?,'%'))
                    OR a.province ILIKE lower(concat('%', ?, '%')) group by 
                    a.id ,a.price  ,
                    a.max_guests   ,
                    a.address      ,
                    a.description  ,
                    a.province     , 
                    a.region       , 
                    a.title 
                """;
        List<AnnouncementResponse> results = jdbcTemplate.query(sql,
                new Object[]{word,word, word, word},
                (rs, rowNum) -> AnnouncementResponse.builder()
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
        return new GlobalSearchResponse(results);
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilters(HouseType houseType, String rating, String price) {
        String sql = "SELECT a.id, a.price, a.max_guests, a.address, a.description, a.province, a.region, a.title, r.rating, "
                + "(SELECT ai.images FROM announcement_images ai WHERE ai.announcement_id = a.id LIMIT 1) as images "
                + "FROM announcements a "
                + "LEFT JOIN feedbacks r ON a.id = r.announcement_id "
                + "WHERE 1=1 ";

        List<Object> params = new ArrayList<>();

        if (houseType != null) {
            sql += "AND a.house_type = ? ";
            params.add(houseType.name());
        }

        if (rating != null && !rating.isEmpty()) {
            sql += "AND r.rating IS NOT NULL ";
        }

        if (price != null && !price.isEmpty()) {
            sql += "AND a.price IS NOT NULL ";
        }

        sql += "GROUP BY a.id, a.price, a.max_guests, a.address, a.description, a.province, a.region, a.title, r.rating, images ";

        if (rating != null && !rating.isEmpty()) {
            sql += "ORDER BY r.rating " + (rating.equalsIgnoreCase("asc") ? "ASC" : "DESC");
        } else if (price != null && !price.isEmpty()) {
            sql += "ORDER BY a.price " + (price.equalsIgnoreCase("asc") ? "ASC" : "DESC");
        }

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
}

    @Override
    public PaginationAnnouncementResponse pagination(Integer page, Integer size) {
        if (page != null && size != null) {
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
            GROUP BY a.id, a.price, a.max_guests, a.address,
                     a.description, a.province, a.region, a.title, a.create_date
            ORDER BY a.create_date 
        """;

            int offset = (page - 1) * size;
            String paginatedSql = sql + " LIMIT ? OFFSET ?";

            List<AnnouncementResponse> announcementResponses = jdbcTemplate.query(
                    paginatedSql,
                    (rs, rowNum) -> AnnouncementResponse.builder()
                            .id(rs.getLong("id"))
                            .price(rs.getInt("price"))
                            .maxGuests(rs.getInt("max_guests"))
                            .address(rs.getString("address"))
                            .description(rs.getString("description"))
                            .province(rs.getString("province"))
                            .title(rs.getString("title"))
                            .images(Collections.singletonList(rs.getString("images")))
                            .rating(rs.getInt("rating"))
                            .build(),
                    size, offset
            );

            return new PaginationAnnouncementResponse(announcementResponses, page, size);
        } else if (page == null && size == null) {
            String sql1 = """
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
            GROUP BY a.id, a.price, a.max_guests, a.address,
                     a.description, a.province, a.region, a.title, a.create_date
            ORDER BY a.create_date 
        """;

            List<AnnouncementResponse> announcementResponses = jdbcTemplate.query(
                    sql1,
                    (rs, rowNum) -> AnnouncementResponse.builder()
                            .id(rs.getLong("id"))
                            .price(rs.getInt("price"))
                            .maxGuests(rs.getInt("max_guests"))
                            .address(rs.getString("address"))
                            .description(rs.getString("description"))
                            .province(rs.getString("province"))
                            .title(rs.getString("title"))
                            .images(Collections.singletonList(rs.getString("images")))
                            .rating(rs.getInt("rating"))
                            .build()
            );

            return new PaginationAnnouncementResponse(announcementResponses);
        }

        return null;
    }
}