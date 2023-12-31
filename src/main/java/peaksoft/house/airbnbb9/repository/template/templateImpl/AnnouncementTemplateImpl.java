package peaksoft.house.airbnbb9.repository.template.templateImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import peaksoft.house.airbnbb9.dto.response.*;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.GlobalSearchResponse;
import peaksoft.house.airbnbb9.dto.response.PaginationAnnouncementResponse;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.*;
import peaksoft.house.airbnbb9.exception.BadCredentialException;
import peaksoft.house.airbnbb9.exception.NotFoundException;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.repository.template.AnnouncementTemplate;


import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnnouncementTemplateImpl implements AnnouncementTemplate {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilter(Status status, HouseType houseType, String rating, PriceType price) {
        String sql = "SELECT a.id, a.price, a.max_guests, a.address, a.description, a.house_type, a.card, a.province, a.region, a.title, r.rating, (SELECT ai.images FROM announcement_images ai WHERE ai.announcement_id = a.id LIMIT 1) as images " +
                "FROM announcements a " +
                "LEFT JOIN feedbacks r ON a.id = r.announcement_id " +
                "WHERE a.price IS NOT NULL";

        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql += " AND a.status = ? ";
            params.add(status.name());
        }
        if (houseType != null) {
            sql += " AND a.house_type = ? ";
            params.add(houseType.name());
        }

        if (rating != null && !rating.isEmpty()) {
            sql += " AND r.rating IS NOT NULL ";
        }

        if (price != null) {
            sql += " ORDER BY a.price ";
            if (price == PriceType.LOW_TO_HIGH) {
                sql += "ASC";
            } else if (price == PriceType.HIGH_TO_LOW) {
                sql += "DESC";
            }
        } else if (rating != null && !rating.isEmpty()) {
            sql += " ORDER BY r.rating ";
            if (rating.equalsIgnoreCase("asc")) {
                sql += "ASC";
            } else {
                sql += "DESC";
            }
        }

        sql += ";";

        return jdbcTemplate.query(sql, params.toArray(), (rs, rowNum) -> AnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .province(rs.getString("province"))
                .title(rs.getString("title"))
                .rating(rs.getInt("rating"))
                .houseType(HouseType.valueOf(rs.getString("house_type")))
                .region(Region.valueOf(rs.getString("region")))
                .card(rs.getBoolean("card"))
                .build());
    }

    @Override
    public PaginationAnnouncementResponse getAllAnnouncementsFilterVendor(Region region, HouseType houseType, String rating, String price, int currentPage, int pageSize) {
        String sql = """
                                SELECT a.id,
                                       a.price,
                                       a.max_guests,
                                       a.address,
                                       a.description,
                                       a.province,
                                       a.region,
                                       a.title,
                                       a.house_type,
                                       a.card,
                                       r.rating,
                                       CASE WHEN f.announcement_id IS NOT NULL THEN true ELSE false END as is_favorite
                                FROM announcements a
                                         LEFT JOIN
                                     favorites f ON a.id = f.announcement_id
                                         LEFT JOIN
                                     feedbacks r ON a.id = r.announcement_id
                                WHERE a.position = 'ACCEPTED'
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
        if (price != null && !price.isEmpty()) {
            sql += "AND a.price IS NOT NULL ";
        }

        sql += "GROUP BY a.id, a.price, a.max_guests, a.address, a.description, a.province, a.region, a.title, r.rating,a.house_type,is_favorite ";

        if (rating != null && !rating.isEmpty()) {
            sql += "ORDER BY r.rating " + (rating.equalsIgnoreCase("Popular") ? "ASC" : "DESC");
        } else if (price != null && !price.isEmpty()) {
            sql += "ORDER BY a.price " + (price.equalsIgnoreCase("Low to high") ? "ASC" : "DESC");
        }
        int offset = (currentPage - 1) * pageSize;
        sql += " LIMIT ? OFFSET ?";
        params.add(pageSize);
        params.add(offset);
        log.info("Filtering announcements for vendors with SQL: " + sql);
        List<AnnouncementResponse> results = jdbcTemplate.query(sql, (rs, rowNum) -> AnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .province(rs.getString("province"))
                .title(rs.getString("title"))
                .rating(rs.getInt("rating"))
                .isFavorite(rs.getBoolean("is_favorite"))
                .region(Region.valueOf(rs.getString("region")))
                .card(rs.getBoolean("card"))
                .houseType(HouseType.valueOf(rs.getString("house_type")))
                .build(), params.toArray());

        log.info("Announcements filtered successfully for vendors!");
        return new PaginationAnnouncementResponse(results, currentPage, pageSize);
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
                       a.card          as card,
                       AVG(r.rating)   as rating
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
                .rating(rs.getInt("rating"))
                .card(rs.getBoolean("card"))
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
                       a.card        as card,
                       AVG(r.rating) AS rating
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
                .card(rs.getBoolean("card"))
                .rating(rs.getInt("rating"))
                .build(), pageSize, offset);

        log.info("Fetched moderated announcements with pagination successfully!");
        return new PaginationAnnouncementResponse(announcementResponses, currentPage, pageSize);
    }


    @Override
    public LatestAnnouncementResponse getLatestAnnouncement() {

        String sql = """
                SELECT 
                       a.id            AS id,
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
                .id(rs.getLong("id"))
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
                SELECT a.id,
                       a.address,
                       a.description,
                       a.title,
                       a.price,
                       AVG(f.rating)                                    AS rating,
                       coalesce((SELECT string_agg(ai.images, ',')
                                 FROM announcement_images ai
                                 WHERE ai.announcement_id = a.id), 'null') AS images
                FROM announcements a
                         LEFT JOIN feedbacks f
                                   ON a.id = f.announcement_id
                WHERE a.house_type = 'HOUSE'
                GROUP BY a.id,
                         a.address,
                         a.description,
                         a.title,
                         a.price
                ORDER BY rating
                        DESC
                LIMIT 3;
                  """;
        log.info("Fetching popular houses.");

        List<PopularHouseResponse> popularHouses = jdbcTemplate.query(sql, (rs, rowNum) -> PopularHouseResponse.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .address(rs.getString("address"))
                .images(Arrays.asList(rs.getString("images").split(",")))
                .price(rs.getInt("price"))
                .rating(rs.getDouble("rating"))
                .build());

        log.info("Fetched popular houses successfully!");
        return popularHouses;
    }

    @Override
    public PopularApartmentResponse getPopularApartment() {
        String sql = """
                SELECT a.id,
                       a.address,
                       a.description,
                       a.title,
                       AVG(f.rating) AS rating,
                       array_agg(ai.images) AS images
                FROM announcements a
                LEFT JOIN announcement_images ai ON a.id = ai.announcement_id
                LEFT JOIN feedbacks f ON a.id = f.announcement_id
                WHERE a.house_type = 'APARTMENT'
                AND a.position ='ACCEPTED'
                GROUP BY
                    a.id,
                    a.address,
                    a.description,
                    a.title
                ORDER BY rating DESC
                LIMIT 1
                    """;
        log.info("Fetching the most popular apartment.");
        PopularApartmentResponse popularApartment = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> PopularApartmentResponse.builder()
                .id(rs.getLong("id"))
                .images(Arrays.asList((String[]) rs.getArray("images").getArray()))
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
                                a.house_type as houseType,
                               a.price as price,
                               a.max_guests as max_guests,
                               a.address as address,
                               a.description as description,
                               a.province as province,
                               a.region as region,
                               a.title as title,
                               a.card as card,
                               AVG(r.rating) as rating
                        FROM announcements a
                                 LEFT JOIN feedbacks r ON a.id = r.announcement_id
                        WHERE a.position = 'ACCEPTED'  AND 
                        (a.region ILIKE lower(concat('%', ?, '%'))
                               OR a.status ILIKE lower(concat('%', ?, '%'))
                               OR a.house_type ILIKE lower(concat('%', ?, '%'))
                               OR a.province ILIKE lower(concat('%', ?, '%')))
                          AND a.latitude BETWEEN ? AND ?
                          AND a.longitude BETWEEN ? AND ?
                        GROUP BY a.id,a.house_type, a.price, a.max_guests, a.address, a.description, a.province, a.region, a.title;
                        """;
                List<AnnouncementResponse> announcementResponses = jdbcTemplate.query(query, (rs, rowNum) -> AnnouncementResponse
                        .builder()
                        .id(rs.getLong("id"))
                        .houseType(HouseType.valueOf(rs.getString("houseType")))
                        .price(rs.getInt("price"))
                        .maxGuests(rs.getInt("max_guests"))
                        .address(rs.getString("address"))
                        .description(rs.getString("description"))
                        .province(rs.getString("province"))
                        .title(rs.getString("title"))
                        .card(rs.getBoolean("card"))
                        .rating(rs.getInt("rating"))
                        .region(Region.valueOf(rs.getString("region")))
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
                a.house_type as houseType,
                       a.price         as price,
                       a.max_guests    as max_guests,
                       a.address       as address,
                       a.description   as description,
                       a.province      as province,
                       a.region        as region,
                       a.title         as title,
                       AVG(r.rating)   as rating
                FROM announcements a
                         LEFT JOIN feedbacks r ON a.id = r.announcement_id
                WHERE a.position = 'ACCEPTED' AND
                  (  a.region ILIKE lower(concat('%', ?, '%'))  
                    OR a.status ILIKE lower(concat('%', ?, '%'))
                    OR a.house_type ILIKE lower(concat('%',?,'%'))
                    OR a.province ILIKE lower(concat('%', ?, '%')) ) group by 
                    a.id,a.house_type ,a.price  ,
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
                .houseType(HouseType.valueOf(rs.getString("houseType")))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .province(rs.getString("province"))
                .title(rs.getString("title"))
                .rating(rs.getInt("rating"))
                .region(Region.valueOf(rs.getString("region")))
                .build(), word, word, word, word);
        log.info("Global search completed successfully!");
        return new GlobalSearchResponse(results);
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
                               AVG(r.rating)   as rating
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
                               AVG(r.rating)   as rating
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
                            .rating(rs.getInt("rating"))
                            .build()
            );

            log.info("Fetched all announcements without pagination successfully!");
            return new PaginationAnnouncementResponse(announcementResponses);
        }

        return null;
    }

    @Override
    public List<AnnouncementResponse> getApplicationById(Long id) {
        String sql = """
                SELECT a.id            as id,
                a.house_type as house_type,
                       a.position      as position,
                       a.price         as price,
                       a.max_guests    as max_guests,
                       a.address       as address,
                       a.description   as description,
                       a.province      as province,
                       a.title         as title,
                       a.card          as card,
                       u.role          as role,
                       u.id            as user_id,
                       u.full_name     as username,
                       u.email         as email,
                       (SELECT ARRAY_AGG(ai.images) FROM announcement_images ai
                    WHERE ai.announcement_id = a.id) as images
                FROM announcements a
                         LEFT JOIN users u ON a.user_id = u.id
                WHERE a.id = ?
                GROUP BY a.id,a.house_type, a.position, a.price, a.max_guests, a.address,
                         a.description, a.province, a.title,
                         u.id, u.full_name, u.email,u.role
                """;
        log.info("Retrieving announcement with ID: {}", id);

        List<AnnouncementResponse> result;
        try {
            result = Collections.singletonList(jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                String position = rs.getString("position");
                if (!"MODERATION".equals(position)) {
                    throw new NotFoundException("Application with ID " + id + " not found.");
                }
                return AnnouncementResponse.builder()
                        .id(rs.getLong("id"))
                        .houseType(HouseType.valueOf(rs.getString("house_type")))
                        .price(rs.getInt("price"))
                        .maxGuests(rs.getInt("max_guests"))
                        .address(rs.getString("address"))
                        .card(rs.getBoolean("card"))
                        .description(rs.getString("description"))
                        .province(rs.getString("province"))
                        .title(rs.getString("title"))
                        .user(User.builder()
                                .id(rs.getLong("user_id"))
                                .fullName(rs.getString("username"))
                                .email(rs.getString("email"))
                                .role(Role.valueOf(rs.getString("role")))
                                .build())
                        .build();
            }, id));

            log.info("Retrieved announcement with ID {} successfully!", id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("Announcement with ID " + id + " not found.");
        }

        return result;
    }

    @Override
    public GetAnnouncementResponse getAnnouncementById(Long announcementId) {
        String query = """
                select a.id as id,
                a.title as title,
                a.house_type as houseType,
                a.max_guests as maxGuests,
                a.address as address,
                a.description as description,
                a.region as region,
                a.province as province,
                u.full_name as fullName,
                u.email as email,
                u.image as image,
                a.price as price ,
                (SELECT string_agg(ai.images, ',')
                        FROM announcement_images ai
                        WHERE ai.announcement_id = a.id) as images
                from announcements a join users u on a.user_id = u.id left join announcement_images ai on a.id = ai.announcement_id where a.id = ?
                group by a.id, a.title, a.house_type, a.max_guests, a.address, a.description, u.full_name, u.email, u.image;
                """;
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> GetAnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .images(Arrays.asList(rs.getString("images").split(",")))
                .houseType(HouseType.valueOf(rs.getString("houseType")))
                .maxGuests(rs.getInt("maxGuests"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .fullName(rs.getString("fullName"))
                .email(rs.getString("email"))
                .region(Region.valueOf(rs.getString("region")))
                .province(rs.getString("province"))
                .image(rs.getString("image"))
                .price(rs.getInt("price"))
                .build(), announcementId);
    }

    @Override
    public AnnouncementsResponseProfile getAnnouncementByIdProfile(Long announcementId) {
        String query = """
                SELECT a.id                                                       AS id,
                       a.title                                                    AS title,
                       ai.images                                                  AS images,
                       a.house_type                                               AS houseType,
                       a.max_guests                                               AS maxGuests,
                       a.address                                                  AS address,
                       a.description                                              AS description,
                       u.full_name                                                AS fullName,
                       u.email                                                    AS email,
                       u.image                                                    AS image,
                       STRING_AGG(DISTINCT u2.full_name, ', ')                    AS bookedByFullName,
                       STRING_AGG(DISTINCT CAST(bu.price_per_day AS TEXT), ', ')  AS priceDay,
                       STRING_AGG(DISTINCT CAST(bu.check_in AS TEXT), ', ')       AS checkIn,
                       STRING_AGG(DISTINCT CAST(bu.check_out AS TEXT), ', ')      AS checkOut,
                       STRING_AGG(DISTINCT u2.email, ', ')                        AS bookedByEmail,
                       STRING_AGG(DISTINCT u3.full_name, ', ')                    AS favoriteByFullName,
                       STRING_AGG(DISTINCT u3.email, ', ')                        AS favoriteByEmail,
                       STRING_AGG(DISTINCT u4.full_name, ', ')                    AS bookedByUserFullName,
                       STRING_AGG(DISTINCT u4.email, ', ')                        AS bookedByEmail,
                       STRING_AGG(DISTINCT fb.comment, ', ')                      AS comments,
                       STRING_AGG(DISTINCT CAST(fb.create_date AS text), ', ')    AS createDateFeedback,
                       STRING_AGG(DISTINCT CAST(fb.rating AS text), ', ')         AS feedbackRating,
                       STRING_AGG(DISTINCT CAST(fb.like_count AS text), ', ')     AS likeCount,
                       STRING_AGG(DISTINCT CAST(fb.dis_like_count AS text), ', ') AS disLikeCount,
                       STRING_AGG(DISTINCT u5.full_name, ', ')                    AS feedbacksUserName,
                       STRING_AGG(DISTINCT u5.image, ', ')                        AS feedbacksImages
                FROM announcements a
                         JOIN users u ON a.user_id = u.id
                         JOIN announcement_images ai ON a.id = ai.announcement_id
                         LEFT JOIN bookings b ON a.id = b.announcement_id
                         LEFT JOIN bookings bu ON u.id = bu.announcement_id
                         LEFT JOIN users u2 ON b.user_id = u2.id
                         LEFT JOIN favorites f ON a.id = f.announcement_id
                         LEFT JOIN users u3 ON f.user_id = u3.id
                         LEFT JOIN feedbacks fb ON a.id = fb.announcement_id
                         LEFT JOIN users u4 ON f.user_id = u4.id
                         LEFT JOIN users u5 ON fb.user_id = u5.id
                WHERE a.id = ?
                GROUP BY a.id, a.title, ai.images, a.house_type, a.max_guests, a.address, a.description, u.full_name, u.email, u.image;
                                                      """;
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
            AnnouncementsResponseProfile.AnnouncementsResponseProfileBuilder builder = AnnouncementsResponseProfile.builder()
                    .id(rs.getLong("id"))
                    .title(rs.getString("title"))
                    .images(Collections.singletonList(rs.getString("images")))
                    .houseType(HouseType.valueOf(rs.getString("houseType")))
                    .maxGuests(rs.getInt("maxGuests"))
                    .address(rs.getString("address"))
                    .description(rs.getString("description"))
                    .fullName(rs.getString("fullName"))
                    .email(rs.getString("email"))
                    .image(rs.getString("image"))
                    .bookedByFullName(rs.getString("bookedByUserFullName"))
                    .bookedByEmail(rs.getString("bookedByEmail"))
                    .priceDay(rs.getInt("priceDay"));

            if (rs.getDate("checkIn") != null) {
                builder.checkIn(rs.getDate("checkIn").toLocalDate());
            }
            if (rs.getDate("checkOut") != null) {
                builder.checkOut(rs.getDate("checkOut").toLocalDate());
            }
            if (rs.getDate("createDateFeedback") != null) {
                builder.createDateFeedback(rs.getDate("createDateFeedback").toLocalDate());
            }

            builder.favoriteByFullName(rs.getString("favoriteByFullName"))
                    .favoriteByEmail(rs.getString("favoriteByEmail"))
                    .comments(rs.getString("comments"))
                    .feedbackRating(rs.getInt("feedbackRating"))
                    .likeCount(rs.getInt("likeCount"))
                    .disLikeCount(rs.getInt("disLikeCount"))
                    .feedbacksUserName(rs.getString("feedbacksUserName"))
                    .feedbacksImages(rs.getString("feedbacksImages"));

            return builder.build();
        }, announcementId);
    }

    @Override
    public AnnouncementsResponseProfile getAnnouncementsByIdAdmin(Long announcementId) {
        String query = """
                SELECT a.id          AS id,
                       a.title       AS title,
                       a.house_type  AS houseType,
                       a.max_guests  AS maxGuests,
                       a.address     AS address,
                       a.description AS description,
                       u.full_name   AS fullName,
                       u.email       AS email,
                       u.image       AS image,
                        (SELECT string_agg(ai.images, ',')
                        FROM announcement_images ai
                        WHERE ai.announcement_id = a.id) as images
                FROM announcements a
                         JOIN users u ON a.user_id = u.id
                         JOIN announcement_images ai ON a.id = ai.announcement_id
                WHERE a.id = ?
                GROUP BY a.id, a.title, a.house_type, a.max_guests, a.address, a.description, u.full_name, u.email, u.image;
                """;
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> AnnouncementsResponseProfile.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .images(Arrays.asList(rs.getString("images").split(",")))
                .houseType(HouseType.valueOf(rs.getString("houseType")))
                .maxGuests(rs.getInt("maxGuests"))
                .address(rs.getString("address"))
                .description(rs.getString("description"))
                .fullName(rs.getString("fullName"))
                .email(rs.getString("email"))
                .image(rs.getString("image"))
                .build(), announcementId);
    }

}