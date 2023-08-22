package peaksoft.house.airbnbb9.mappers;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.AnnouncementInnerPageResponse;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnnouncementViewMapper {

    private final JdbcTemplate jdbcTemplate;

    public Double calculateRating() {
        String sql = """
                 SELECT
                     AVG(f.rating) AS rating
                 FROM users u
                          JOIN announcements a ON u.id = a.user_id
                          JOIN feedbacks f ON a.id = f.announcement_id
                 """;

        log.info("Calculating average rating for announcements");

        Double averageRating = jdbcTemplate.queryForObject(sql, Double.class);

        log.info("Calculated average rating successfully: " + averageRating);

        return averageRating;
    }
    public Double calculateRating2() {
        String sql = """
                 SELECT
                     AVG(f.rating) AS rating
                 FROM users u
                          JOIN announcements a ON u.id = a.user_id
                          JOIN feedbacks f ON a.id = f.announcement_id
                 WHERE a.position = 'MODERATION'
                 """;

        log.info("Calculating average rating for announcements with status 'MODERATION'");

        Double averageRating = jdbcTemplate.queryForObject(sql, Double.class);

        log.info("Calculated average rating successfully: " + averageRating);

        return averageRating;
    }
    public AnnouncementInnerPageResponse entityToDtoConverting(Announcement announcement) {
        if (announcement == null) {
            return null;
        }
        log.info("Converting Announcement entity to AnnouncementInnerPageResponse");

        AnnouncementInnerPageResponse response = new AnnouncementInnerPageResponse();
        response.setId(announcement.getId());
        response.setImages(announcement.getImages());
        response.setHouseType(announcement.getHouseType());
        response.setMaxGuests(announcement.getMaxGuests());
        response.setAddress(announcement.getAddress());
        response.setDescription(announcement.getDescription());
        response.setPrice(BigDecimal.valueOf(announcement.getPrice()));
        response.setUserID(announcement.getUser().getId());
        response.setUserImage(announcement.getUser().getImage());
        response.setUserFullName(announcement.getUser().getFullName());
        response.setUserEmail(announcement.getUser().getEmail());

        log.info("Converted Announcement entity to AnnouncementInnerPageResponse successfully");
        return response;
    }
    private void dtoToEntityConverting(AnnouncementRequest request, Announcement announcement) {
        announcement.setTitle(request.getTitle());
        announcement.setDescription(request.getDescription());
        announcement.setImages(request.getImages());
        announcement.setStatus(announcement.getStatus());
        announcement.setPrice(request.getPrice());
        announcement.setMaxGuests(request.getMaxGuests());
        announcement.setHouseType(request.getHouseType());
        announcement.setCreateDate(LocalDate.now());
        announcement.setRegion(request.getRegion());
        announcement.setAddress(request.getAddress());
        announcement.setProvince(request.getProvince());
    }
    public void updateAnnouncement(Announcement announcement, AnnouncementRequest request) {
        dtoToEntityConverting(request, announcement);
    }
}