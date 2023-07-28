package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.AllAnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.Feedback;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.exceptoin.NotFoundException;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
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
        return announcementRepository.getAll();
    }

    @Override
    public SimpleResponse deleteByIdAnnouncement(Long announcementId) {
        if (announcementRepository.existsById(announcementId)) {
            announcementRepository.deleteById(announcementId);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
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

        return jdbcTemplate.query(sql, (rs, rowNum) -> AnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .title(rs.getString("title"))
                .images(Collections.singletonList(rs.getString("images")))
                .status(Status.valueOf(rs.getString("status")))
                .build());
    }


    @Override
    public List<AnnouncementResponse> getAllAnnouncementsThePopular(String popular) {
        String sql = "SELECT * FROM announcements a " +
                "                LEFT JOIN announcement_images ai ON a.id = ai.announcement_id " +
                "                 JOIN feedbacks r ON a.id = r.announcement_id " +
                "                ORDER BY r.rating DESC LIMIT 2";

        return jdbcTemplate.query(sql, (rs, rowNum) -> AnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .title(rs.getString("title"))
                .images(Collections.singletonList(rs.getString("images")))
                .status(Status.valueOf(rs.getString("status")))
                .rating(rs.getInt("rating"))
                .build());
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsTheLasted() {
        String sql = "SELECT * FROM announcements a\n" +
                "                LEFT JOIN announcement_images ai ON a.id = ai.announcement_id\n" +
                "                 JOIN feedbacks r ON a.id = r.announcement_id\n" +
                "                ORDER BY r.rating ASC LIMIT 2 ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> AnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .title(rs.getString("title"))
                .images(Collections.singletonList(rs.getString("images")))
                .status(Status.valueOf(rs.getString("status")))
                .rating(rs.getInt("rating"))
                .build());
    }


    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilterByHomeType(HouseType houseType) {
        String sql = "SELECT a.id, a.price, a.max_guests, a.address, a.title, ai.images, a.status\n" +
                "FROM announcements a " +
                "LEFT JOIN announcement_images ai ON a.id = ai.announcement_id WHERE a.house_type = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> AnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .title(rs.getString("title"))
                .images(Collections.singletonList(rs.getString("images")))
                .status(Status.valueOf(rs.getString("status")))
                .build());
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilterByPriceHighToLow(String highToLow) {
        String sql = "SELECT * FROM announcements a LEFT JOIN announcement_images ai ON a.id = ai.announcement_id\n" +
                "JOIN feedbacks r ON a.id = r.announcement_id\n" +
                "ORDER BY a.price DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> AnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .title(rs.getString("title"))
                .images(Collections.singletonList(rs.getString("images")))
                .status(Status.valueOf(rs.getString("status")))
                .rating(rs.getInt("rating"))
                .build());
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilterByPriceLowToHigh() {
        String sql = "SELECT * FROM announcements a LEFT JOIN announcement_images ai ON a.id = ai.announcement_id\n" +
                "JOIN feedbacks r ON a.id = r.announcement_id\n" +
                "ORDER BY a.price ASC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> AnnouncementResponse.builder()
                .id(rs.getLong("id"))
                .price(rs.getInt("price"))
                .maxGuests(rs.getInt("max_guests"))
                .address(rs.getString("address"))
                .title(rs.getString("title"))
                .images(Collections.singletonList(rs.getString("images")))
                .status(Status.valueOf(rs.getString("status")))
                .rating(rs.getInt("rating"))
                .build());
    }
}
