package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.*;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.exceptoin.NotFoundException;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.dto.response.AllAnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.entity.Feedback;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.repository.template.AnnouncementTemplate;
import peaksoft.house.airbnbb9.service.AnnouncementService;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final JdbcTemplate jdbcTemplate;
    private final AnnouncementTemplate announcementTemplate;

    @Override
    public AllAnnouncementResponse getByIdAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() ->
                new NotFoundException("Announcement with id: " + announcementId + " is no exist!"));
        List<Feedback> feedbacks = announcementRepository.getAllAnnouncementFeedback(announcementId);
        return AllAnnouncementResponse.builder()
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
                .isFeedback(feedbacks.size())
                .build();
    }

    @Override
    public AnnouncementResponse updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() ->
                new NotFoundException(" Announcement with id: " + announcementId + " is no exist!"));
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
        return announcementTemplate.getAllAnnouncements();
    }


    public SimpleResponse deleteByIdAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() -> new NotFoundException("Announcement with id: " + announcementId + " does not exist!"));
        announcementRepository.delete(announcement);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(String.format("Announcement with id: " + announcementId + " deleted..."))
                .build();
    }


    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilter(Status status, HouseType houseType,String rating, String price) {
        return announcementTemplate.getAllAnnouncementsFilter(status, houseType,rating,price);
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilterVendor(Region region, HouseType houseType, String rating, String price) {
        return announcementTemplate.getAllAnnouncementsFilterVendor(region,houseType,rating,price);
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

    @Override
    public SimpleResponse approveAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() ->
                new NotFoundException("Announcement with id: " + announcementId + " is no exist!"));
        if (announcement != null) {
            announcement.setStatus(Status.NOT_BOOKED);
            announcementRepository.save(announcement);
            return new SimpleResponse("Announcement approved successfully!", HttpStatus.OK);
        } else {
            throw new NotFoundException("Announcement not found");
        }
    }

    @Override
    public SimpleResponse rejectAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() ->
                new NotFoundException("Announcement with id: " + announcementId + " is no exist!"));
        if (announcement != null) {
            announcementRepository.delete(announcement);
            return new SimpleResponse("Announcement rejected successfully!", HttpStatus.OK);
        } else {
            throw new NotFoundException("Announcement not found");
        }
    }

    @Override
    public PaginationAnnouncementResponse getAllAnnouncementsModerationAndPagination(int currentPage, int pageSize) {
        return announcementTemplate.getAllAnnouncementsModerationAndPagination(currentPage, pageSize);
    }

    @Override
    public LastestAnnouncementResponse getLastestAnnouncement() {
        return announcementTemplate.getLastestAnnouncement();
    }

    @Override
    public List<PopularHouseResponse> getPopularHouses() {
        return announcementTemplate.getPopularHouses();
    }

    public PopularApartmentResponse getPopularApartment(){
        return announcementTemplate.getPopularApartment();
    }
}