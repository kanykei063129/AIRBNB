package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.SimpleResponse;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.AllAnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.Feedback;
import peaksoft.house.airbnbb9.exceptoin.NotFoundException;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.service.AnnouncementService;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Status;

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
        announcement.setHouseType(announcementRequest.houseType());
        announcement.setImages(announcementRequest.images());
        announcement.setPrice(announcementRequest.price());
        announcement.setRegion(announcementRequest.region());
        announcement.setAddress(announcementRequest.address());
        announcement.setDescription(announcementRequest.description());
        announcement.setStatus(announcementRequest.status());
        announcement.setTitle(announcementRequest.title());
        announcement.setMaxGuests(announcementRequest.maxGuests());
        announcement.setProvince(announcementRequest.province());
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
        } else throw new NoSuchElementException(String.format("Announcement with id:%s does not exist", announcementId));
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
}
