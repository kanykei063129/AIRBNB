package peaksoft.house.airbnbb9.repository.template.templateImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.response.FavoriteAnnouncementsResponse;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.exception.NotFoundException;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.repository.template.FavoriteTemplate;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FavoriteTemplateImpl implements FavoriteTemplate {

    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    private User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        log.info("Getting authentication for user with email: " + email);

        User user = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email: " + email + "doesn't exists!"));

        log.info("Got authentication for user with email: " + email + " successfully!");

        return user;
    }

    @Override
    public List<FavoriteAnnouncementsResponse> getAllFavoriteAnnouncements() {
        if (getAuthentication().getFavorites().isEmpty()) {
            throw new NotFoundException("Unfortunately you dont have favorite announcements ");
        }
        String sql = "select a.id,\n" +
                "       (select ai.images\n" +
                "        from announcement_images ai\n" +
                "        where ai.announcement_id = a.id\n" +
                "        limit 1),\n" +
                "       a.price,\n" +
                "       (select sum(f.rating) / count(f) from feedbacks f where f.announcement_id = a.id) as rating,\n" +
                "       a.description,\n" +
                "       a.address,\n" +
                "       a.max_guests,\n" +
                "       a.status,\n" +
                "       case when f.announcement_id is not null then true else false end as is_favorite\n" +
                "from announcements a\n" +
                "         join favorites f on f.announcement_id = a.id\n" +
                "         and f.user_id = ?";

        log.info("Fetching all favorite announcements.");

        List<FavoriteAnnouncementsResponse> favoriteAnnouncements = jdbcTemplate.query(sql, (rs, rowNum) -> new FavoriteAnnouncementsResponse(
                rs.getLong("id")
                , rs.getString("images")
                , rs.getInt("price")
                , rs.getString("address")
                , rs.getString("description")
                , Status.valueOf(rs.getString("status"))
                , rs.getInt("max_guests")
                , rs.getDouble("rating")
                , rs.getBoolean("is_favorite")
        ), getAuthentication().getId());

        log.info("Fetched all favorite announcements successfully!");
        return favoriteAnnouncements;
    }
}