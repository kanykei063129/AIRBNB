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

import java.util.ArrayList;
import java.util.Collections;
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
        List<FavoriteAnnouncementsResponse> favorites = new ArrayList<>();
        if (getAuthentication().getFavorites().isEmpty()) {
            return favorites;
        }
        String sql = """
                select a.id,
                       (select ai.images
                        from announcement_images ai
                        where ai.announcement_id = a.id
                        limit 1),
                       a.price,
                       (select sum(f.rating) / count(f) from feedbacks f where f.announcement_id = a.id) as rating,
                       a.description,
                       a.address,
                       a.max_guests,
                       a.status,
                       case when f.announcement_id is not null then true else false end                  as is_favorite
                from announcements a
                         join favorites f on f.announcement_id = a.id
                    and f.user_id = ?;
                """;

        log.info("Fetching all favorite announcements.");

        List<FavoriteAnnouncementsResponse> favoriteAnnouncements = jdbcTemplate.query(sql, (rs, rowNum) -> new FavoriteAnnouncementsResponse(
                rs.getLong("id")
                , Collections.singletonList(rs.getString("images"))
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