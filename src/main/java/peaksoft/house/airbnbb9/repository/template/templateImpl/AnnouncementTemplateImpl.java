package peaksoft.house.airbnbb9.repository.template.templateImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.dto.response.*;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.GlobalSearchResponse;
import peaksoft.house.airbnbb9.dto.response.PaginationAnnouncementResponse;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.*;
import peaksoft.house.airbnbb9.exception.BadCredentialException;
import peaksoft.house.airbnbb9.exception.NotFoundException;
import peaksoft.house.airbnbb9.repository.template.AnnouncementTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AnnouncementTemplateImpl implements AnnouncementTemplate {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilter(Status status, HouseType houseType, String rating, String price) {
        String sql = """
                SELECT a.id,
                       a.price,
                       a.max_guests,
                       a.address,
                       a.description,
                       a.province,
                       a.region,
                       a.title,
                       r.rating,
                       (SELECT ai.images FROM announcement_images ai WHERE ai.announcement_id = a.id LIMIT 1) as images
                FROM announcements a
                         LEFT JOIN feedbacks r ON a.id = r.announcement_id
                WHERE 1 = 1
                """;
        log.info("Starting to filter announcements.");

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
        log.info("Filtering announcements with SQL: " + sql);

        List<AnnouncementResponse> results = jdbcTemplate.query(sql, params.toArray(), (rs, rowNum) -> AnnouncementResponse.builder()
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

        log.info("Announcements filtered successfully!");
        return results;
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilterVendor(Region region, HouseType houseType, String rating, String price) {
        String sql = """
                                SELECT a.id,
                                       a.price,
                                       a.max_guests,
                                       a.address,
                                       a.description,
                                       a.province,
                                       a.region,
                                       a.title,
                                       r.rating,
                                       CASE WHEN f.announcement_id IS NOT NULL THEN true ELSE false END                       as is_favorite,
                                       (SELECT ai.images FROM announcement_images ai WHERE ai.announcement_id = a.id LIMIT 1) as images
                                FROM announcements a
                                         LEFT JOIN
                                     favorites f ON a.id = f.announcement_id
                                         LEFT JOIN
                                     feedbacks r ON a.id = r.announcement_id
                                WHERE 1 = 1
                """;
        log.info("Starting to filter announcements for vendors.");

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

        sql += "GROUP BY a.id, a.price, a.max_guests, a.address, a.description, a.province, a.region, a.title, r.rating, images,is_favorite ";

        if (rating != null && !rating.isEmpty()) {
            sql += "ORDER BY r.rating " + (rating.equalsIgnoreCase("asc") ? "ASC" : "DESC");
        } else if (price != null && !price.isEmpty()) {
            sql += "ORDER BY a.price " + (price.equalsIgnoreCase("asc") ? "ASC" : "DESC");
        }
        log.info("Filtering announcements for vendors with SQL: " + sql);

        List<AnnouncementResponse> results = jdbcTemplate.query(sql, params.toArray(), (rs, rowNum) -> AnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .province(rs.getString("province"))
                .title(rs.getString("title"))
                .images(Collections.singletonList(rs.getString("images")))
                .rating(rs.getInt("rating"))
                .isFavorite(rs.getBoolean("is_favorite"))
                .build());

        log.info("Announcements filtered successfully for vendors!");
        return results;
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
                         WHERE a.position ='ACCEPTED'
                GROUP BY a.id, a.price, a.max_guests, a.address,
                         a.description, a.province, a.region, a.title
                             
                 """;
        log.info("Retrieving all accepted announcements.");

        List<AnnouncementResponse> results = jdbcTemplate.query(sql, (rs, rowNum) -> AnnouncementResponse.builder()
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

        log.info("Retrieved all accepted announcements successfully!");
        return results;
    }

    @Override
    public PaginationAnnouncementResponse getAllAnnouncementsModerationAndPagination(int currentPage, int pageSize) {
        String sql = """
                SELECT a.id          AS id,
                       a.price       AS price,
                       a.max_guests  AS max_guests,
                       a.address     AS address,
                       a.description AS description,
                       a.province    AS province,
                       a.title       AS title,
                       a.position    AS position,
                       AVG(r.rating) AS rating,
                       (SELECT ai.images
                        FROM announcement_images ai
                        WHERE ai.announcement_id = a.id
                        LIMIT 1)     AS images
                FROM announcements a
                         LEFT JOIN feedbacks r ON a.id = r.announcement_id
                WHERE a.position = 'MODERATION'
                GROUP BY a.id, a.price, a.max_guests, a.address,
                         a.description, a.province, a.region, a.title, a.create_date
                ORDER BY a.create_date
                                 """;
        int offset = (currentPage - 1) * pageSize;
        sql += "LIMIT ? OFFSET ?";

        log.info("Fetching moderated announcements with pagination.");

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

        log.info("Fetched moderated announcements with pagination successfully!");
        return new PaginationAnnouncementResponse(announcementResponses, currentPage, pageSize);
    }


    @Override
    public LatestAnnouncementResponse getLatestAnnouncement() {

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
        log.info("Fetching the latest announcement.");

        LatestAnnouncementResponse latestAnnouncement = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> LatestAnnouncementResponse.builder()
                .images(Arrays.asList(rs.getString("images").split(",")))
                .title(rs.getString("title"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .build());

        log.info("Fetched the latest announcement successfully!");
        return latestAnnouncement;
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
        log.info("Fetching popular houses.");

        List<PopularHouseResponse> popularHouses = jdbcTemplate.query(sql, (rs, rowNum) -> PopularHouseResponse.builder()
                .title(rs.getString("title"))
                .image(rs.getString("image"))
                .address(rs.getString("address"))
                .price(rs.getInt("price"))
                .rating(rs.getDouble("rating"))
                .build());

        log.info("Fetched popular houses successfully!");
        return popularHouses;
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
        log.info("Fetching the most popular apartment.");
        PopularApartmentResponse popularApartment = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> PopularApartmentResponse.builder()
                .images(Arrays.asList(rs.getString("images").split(",")))
                .title(rs.getString("title"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .build());
        log.info("Fetched the most popular apartment successfully!");
        return popularApartment;
    }

    @Override
    public GlobalSearchResponse search(String word, boolean isNearby, Double latitude, Double longitude) {
        if (isNearby) {
            if (latitude != null && longitude != null) {
                double earthRadius = 6371;
                double distance = 5;
                double latRange = Math.toDegrees(distance / earthRadius);
                double longRange = Math.toDegrees(distance / (earthRadius * Math.cos(Math.toRadians(latitude))));
                double minLat = latitude - latRange;
                double maxLat = latitude + latRange;
                double minLong = longitude - longRange;
                double maxLong = longitude + longRange;
                String query = """
                        SELECT a.id as id,
                               a.price as price,
                               a.max_guests as max_guests,
                               a.address as address,
                               a.description as description,
                               a.province as province,
                               a.region as region,
                               a.title as title,
                               AVG(r.rating) as rating,
                               (SELECT ai.images
                                FROM announcement_images ai
                                WHERE ai.announcement_id = a.id
                                LIMIT 1) as images
                        FROM announcements a
                                 LEFT JOIN feedbacks r ON a.id = r.announcement_id
                        WHERE (a.region ILIKE lower(concat('%', ?, '%'))
                               OR a.status ILIKE lower(concat('%', ?, '%'))
                               OR a.house_type ILIKE lower(concat('%', ?, '%'))
                               OR a.province ILIKE lower(concat('%', ?, '%')))
                          AND a.latitude BETWEEN ? AND ?
                          AND a.longitude BETWEEN ? AND ?
                        GROUP BY a.id, a.price, a.max_guests, a.address, a.description, a.province, a.region, a.title;
                        """;
                List<AnnouncementResponse> announcementResponses = jdbcTemplate.query(query, (rs, rowNum) -> AnnouncementResponse
                        .builder()
                        .id(rs.getLong("id"))
                        .price(rs.getInt("price"))
                        .maxGuests(rs.getInt("max_guests"))
                        .address(rs.getString("address"))
                        .description(rs.getString("description"))
                        .province(rs.getString("province"))
                        .title(rs.getString("title"))
                        .images(Collections.singletonList(rs.getString("images")))
                        .rating(rs.getInt("rating"))
                        .build(), word, word, word, word, minLat, maxLat, minLong, maxLong);
                log.info(String.format("Performing global search with key word:%s and get nearby user's location", word));
                return new GlobalSearchResponse(announcementResponses);
            } else {
                log.error("Latitude and longitude is null");
                throw new BadCredentialException("Latitude and longitude is null");
            }
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
        log.info("Performing global search with keyword: " + word);
        List<AnnouncementResponse> results = jdbcTemplate.query(sql, (rs, rowNum) -> AnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .province(rs.getString("province"))
                .title(rs.getString("title"))
                .images(Collections.singletonList(rs.getString("images")))
                .rating(rs.getInt("rating"))
                .build(), word, word, word, word);
        log.info("Global search completed successfully!");
        return new GlobalSearchResponse(results);
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilters(HouseType houseType, String rating, PriceType price) {
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

        if (price != null) {
            sql += "AND a.price IS NOT NULL ";
        }

        sql += "GROUP BY a.id, a.price, a.max_guests, a.address, a.description, a.province, a.region, a.title, r.rating, images ";

        if (rating != null && !rating.isEmpty()) {
            sql += "ORDER BY r.rating " + (rating.equalsIgnoreCase("asc") ? "ASC" : "DESC");
        } else if (price != null && !price.equals(PriceType.LOW_TO_HIGH)) {
            sql += "ORDER BY a.price " + (price.equals("LOW_TO_HIGH") ? "ASC" : "DESC");
        } else if (price != null && !price.equals(PriceType.HIGH_TO_LOW)) {
            sql += "ORDER BY a.price " + (price.equals("HIGH_TO_LOW") ? "DESC" : "ASC");
        }

        log.info("Fetching announcements with filters: HouseType - " + houseType + ", Rating - " + rating + ", PriceType - " + price);

        List<AnnouncementResponse> results = jdbcTemplate.query(sql, params.toArray(), (rs, rowNum) -> AnnouncementResponse.builder()
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

        log.info("Fetched announcements with filters successfully!");
        return results;
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

            log.info("Fetching paginated announcements. Page: " + page + ", Size: " + size);

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

            log.info("Fetched paginated announcements successfully!");
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

            log.info("Fetching all announcements without pagination.");

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

            log.info("Fetched all announcements without pagination successfully!");
            return new PaginationAnnouncementResponse(announcementResponses);
        }

        return null;
    }

    @Override
    public AnnouncementResponse getApplicationById(Long id) {
        String sql = """
                SELECT a.id            as id,
                       a.position      as position,
                       a.price         as price,
                       a.max_guests    as max_guests,
                       a.address       as address,
                       a.description   as description,
                       a.province      as province,
                       a.title         as title,
                       u.role          as role,
                       u.id            as user_id,
                       u.full_name     as username,
                       u.email         as email,
                       (SELECT ARRAY_AGG(ai.images) FROM announcement_images ai
                    WHERE ai.announcement_id = a.id) as images
                FROM announcements a
                         LEFT JOIN users u ON a.user_id = u.id
                WHERE a.id = ?
                GROUP BY a.id, a.position, a.price, a.max_guests, a.address,
                         a.description, a.province, a.title,
                         u.id, u.full_name, u.email,u.role
                """;
        log.info("Retrieving announcement with ID: {}", id);

        AnnouncementResponse result;
        try {
            result = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                String position = rs.getString("position");
                if (!"MODERATION".equals(position)) {
                    throw new NotFoundException("Application with ID " + id + " not found.");
                }
                return AnnouncementResponse.builder()
                        .id(rs.getLong("id"))
                        .price(rs.getInt("price"))
                        .maxGuests(rs.getInt("max_guests"))
                        .address(rs.getString("address"))
                        .description(rs.getString("description"))
                        .province(rs.getString("province"))
                        .title(rs.getString("title"))
                        .images(Collections.singletonList(rs.getString("images")))
                        .user(User.builder()
                                .id(rs.getLong("user_id"))
                                .fullName(rs.getString("username"))
                                .email(rs.getString("email"))
                                .role(Role.valueOf(rs.getString("role")))
                                .build())
                        .build();
            }, id);

            log.info("Retrieved announcement with ID {} successfully!", id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("Announcement with ID " + id + " not found.");
        }

        return result;
    }

    @Override
    public GetAnnouncementResponse getAnnouncementById(Long announcementId) {
        String query = """
                select distinct a.id as id,
                a.title as title,
                ai.images as images,
                a.house_type as houseType,
                a.max_guests as maxGuests,
                a.address as address,
                a.description as description,
                u.full_name as fullName,
                u.email as email,
                u.image as image from announcements a join users u on a.user_id = u.id join announcement_images ai on a.id = ai.announcement_id where a.id = ?;
                """;
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> GetAnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .images(Collections.singletonList(rs.getString("images")))
                .houseType(HouseType.valueOf(rs.getString("houseType")))
                .maxGuests(rs.getInt("maxGuests"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .fullName("fullName")
                .email("email")
                .image("image")
                .build(), announcementId);
    }

    @Override
    public AnnouncementsResponseProfile getAnnouncementsByIdProfile(Long announcementId) {
        String query = """
                SELECT
                    a.id AS announcement_id,
                    a.title,
                    ai.images,
                    a.house_type,
                    a.max_guests,
                    a.address,
                    a.description,
                    u.full_name AS owner_full_name,
                    u.email AS owner_email,
                    u.image AS owner_image,
                    STRING_AGG(DISTINCT u2.full_name, ', ') AS booked_by_full_name,
                    STRING_AGG(DISTINCT u2.email, ', ') AS booked_by_email,
                    STRING_AGG(DISTINCT u3.full_name, ', ') AS favorited_by_full_name,
                    STRING_AGG(DISTINCT u3.email, ', ') AS favorited_by_email
                FROM announcements a
                         JOIN users u ON a.user_id = u.id
                         JOIN announcement_images ai ON a.id = ai.announcement_id
                         LEFT JOIN bookings b ON a.id = b.announcement_id
                         LEFT JOIN users u2 ON b.user_id = u2.id
                         LEFT JOIN favorites f ON a.id = f.announcement_id
                         LEFT JOIN users u3 ON f.user_id = u3.id
                WHERE a.id = ?
                GROUP BY a.id, a.title, ai.images, a.house_type, a.max_guests, a.address, a.description, u.full_name, u.email, u.image;;
                                """;

        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> AnnouncementsResponseProfile.builder()
                .id(rs.getLong("announcement_id"))
                .title(rs.getString("title"))
                .images(Collections.singletonList(rs.getString("images")))
                .houseType(HouseType.valueOf(rs.getString("house_type")))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .fullName(rs.getString("owner_full_name"))
                .email(rs.getString("owner_email"))
                .image(rs.getString("owner_image"))
                .bookedByFullName(Collections.singletonList(rs.getString("booked_by_full_name")))
                .bookedByEmail(Collections.singletonList(rs.getString("booked_by_email")))
                .favoriteByFullName(Collections.singletonList(rs.getString("favorited_by_full_name")))
                .favoriteByEmail(Collections.singletonList(rs.getString("favorited_by_email")))
                .build(), announcementId);
    }

}