package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.responce.*;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.exceptoin.NotFoundException;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.service.AnnouncementService;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
@Service
@Transactional
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public AnnouncementResponse updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() -> new NotFoundException(" Announcement with id: " + announcementId + " is no exist!"));
        announcement.setHouseType(announcementRequest.getHouseType());
        announcement.setImages(announcementRequest.getImages());
        announcement.setPrice(announcementRequest.getPrice());
        announcement.setRegion(announcementRequest.getRegion());
        announcement.setAddress(announcementRequest.getAddress());
        announcement.setDescription(announcementRequest.getDescription());
        announcement.setStatus(announcementRequest.getStatus());
        announcement.setTitle(announcementRequest.getTitle());
        announcement.setMaxGuests(announcementRequest.getMaxGuests());
        announcement.setProvince(announcementRequest.getProvince());
        announcementRepository.save(announcement);
        return AnnouncementResponse.builder()
                .id(announcement.getId())
                .houseType(announcement.getHouseType())
                .images(announcement.getImages())
                .price(announcement.getPrice())
                .region(announcement.getRegion())
                .address(announcement.getAddress())
                .description(announcement.getDescription())
                .status(announcement.getStatus())
                .title(announcement.getTitle())
                .maxGuests(announcement.getMaxGuests())
                .province(announcement.getProvince())
                .build();
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncements() {
        return announcementRepository.getAll();
    }

    @Override
    public SimpleResponse deleteByIdAnnouncement(Long announcementId) {
        if (announcementRepository.existsById(announcementId)) {
            announcementRepository.deleteById(announcementId);
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Successfully deleted...")
                    .build();
        } else
            throw new NoSuchElementException(String.format("Announcement with id:%s does not exist", announcementId));
    }

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
    @Override
        public List<BookingResponse> getAllAnnouncementsBookings(Long userId) {
            String sql = "SELECT b.bookingId, b.announcementId,b.userId,\n" +
                    "       u.full_name, u.email\n" +
                    "FROM bookings b\n" +
                    "LEFT JOIN Users u ON u.id = b.user_id\n" +
                    "WHERE u.id = ?";

            return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
                BookingResponse bookingResponse = new BookingResponse();
                bookingResponse.setBookingId(rs.getLong("bookingId"));
                bookingResponse.setUserId(rs.getLong("userId"));
                bookingResponse.setAnnouncementId(rs.getLong("announcementId"));
                return bookingResponse;
        });
    }

    @Override
    public List<AnnouncementResponse> getAllMyAnnouncements(Long userId) {
        String sql = "SELECT a.id, a.images, a.price, a.region, a.address, a.description, a.max_guests\n" +
                "FROM announcements a\n" +
                "JOIN Users u ON u.id = a.user_id\n" +
                "WHERE u.id = ?";

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> new AnnouncementResponse(
                rs.getLong("id"),
                Collections.singletonList(rs.getString("images")),
                rs.getInt("price"),
                Region.valueOf(rs.getString("region")),
                rs.getString("address"),
                rs.getString("description"),
                rs.getInt("max_guests")
        ));
    }

    @Override
    public List<PaginationBookingResponse> getAllAnnouncementsBookingsSortAndPagination(String ascOrDesc, int currentPage, int pageSize) {
        int offset = (currentPage - 1) * pageSize;
        String sql = "SELECT u.id as userId, u.full_name as fullName, u.email,\n" +
                "       COUNT(DISTINCT b.id) as bookings\n" +
                "FROM Users u\n" +
                "LEFT JOIN bookings b ON u.id = b.user_id\n" +
                "GROUP BY u.id, u.full_name\n" +
                "ORDER BY bookings " + (ascOrDesc.equalsIgnoreCase("desc") ? "DESC" : "ASC") + "\n" +
                "LIMIT " + pageSize + " OFFSET " + offset;

        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(PaginationBookingResponse.class));
    }

    @Override
    public List<PaginationAnnouncementResponse> getAllMyAnnouncementsSortAndPagination(String ascOrDesc, int currentPage, int pageSize) {
        int offset = (currentPage - 1) * pageSize;

        String sql = "SELECT u.id as userId, u.full_name as fullName, u.email,\n" +
                "       COUNT(DISTINCT a.id) as announcements\n" +
                "FROM Users u\n" +
                "LEFT JOIN announcements a ON u.id = a.user_id\n" +
                "GROUP BY u.id, u.full_name\n" +
                "ORDER BY announcements " + (ascOrDesc.equalsIgnoreCase("desc") ? "DESC" : "ASC") + "\n" +
                "LIMIT " + pageSize + " OFFSET " + offset;

        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(PaginationAnnouncementResponse.class));
    }
}