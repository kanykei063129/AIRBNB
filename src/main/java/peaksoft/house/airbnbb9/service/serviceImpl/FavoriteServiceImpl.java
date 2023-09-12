package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.response.FavoriteAnnouncementsResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.Favorite;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.exception.NotFoundException;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.repository.FavoriteRepository;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.repository.template.FavoriteTemplate;
import peaksoft.house.airbnbb9.service.FavoriteService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FavoriteServiceImpl implements FavoriteService {

    private final UserRepository userRepository;
    private final FavoriteTemplate favoriteTemplate;
    private final FavoriteRepository favoriteRepository;
    private final AnnouncementRepository announcementRepository;

    private User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("Getting authentication details for user with email: {}", email);

        return userRepository.getUserByEmail(email).orElseThrow(() -> {
            log.error("User with email: {} not found!", email);
            return new NotFoundException("User with email: " + email + " doesn't exist!");
        });
    }

    @Override
    public List<FavoriteAnnouncementsResponse> getAllFavoriteAnnouncements() {
        return favoriteTemplate.getAllFavoriteAnnouncements();
    }

    @Override
    public SimpleResponse addOrRemoveFavorite(Long announcementId) {
        boolean isTrue = false;
        Favorite favorite = new Favorite();
        User user = getAuthentication();
        log.info("Adding or removing favorite for user with email: {}", user.getEmail());

        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() -> new NotFoundException("Announcement with id: " + announcementId + " doesn't exist "));
        for (Favorite f : user.getFavorites()) {
            if (!Objects.equals(f.getAnnouncement().getId(), announcement.getId())) {
                isTrue = true;
            } else {
                favoriteRepository.delete(f);
                log.info("Announcement with id: {} was deleted from favorites for user: {}", announcementId, user.getEmail());
                return SimpleResponse.builder().httpStatus(HttpStatus.OK).message(String.format("Announcement with id: " + announcementId + " was deleted from your favorites.")).build();
            }
        }
        if (isTrue) {
            favorite.setAnnouncement(announcement);
            favorite.setUser(user);
            favoriteRepository.save(favorite);

            log.info("Announcement with id: {} was added to favorites for user: {}", announcementId, user.getEmail());

            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message(String.format("Announcement with id: " + announcementId + " was added to your favorites!")).build();
        }
        return null;
    }
}