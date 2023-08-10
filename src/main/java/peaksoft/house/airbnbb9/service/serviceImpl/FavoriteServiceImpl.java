package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.response.FavoriteAnnouncementsResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.Favorite;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.exceptoin.NotFoundException;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.repository.FavoriteRepository;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.repository.template.FavoriteTemplate;
import peaksoft.house.airbnbb9.service.FavoriteService;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    private final UserRepository userRepository;
    private final FavoriteTemplate favoriteTemplate;
    private final FavoriteRepository favoriteRepository;
    private final AnnouncementRepository announcementRepository;

    private User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email: " + email + "doesn't exists!"));
    }

    @Override
    public List<FavoriteAnnouncementsResponse> getAllFavoriteAnnouncements() {
        return favoriteTemplate.getAllFavoriteAnnouncements();
    }

    @Override
    public SimpleResponse addOrRemoveFavorite(Long announcementId) {
        boolean isTrue;
        Favorite favorite = new Favorite();
        User user = getAuthentication();
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() ->
                new NotFoundException("Announcement with id: " + announcementId + " doesn't exist "));
        for (Favorite f :
                user.getFavorites()) {
            if (f.getAnnouncement().getId() != announcement.getId()) {
                isTrue = true;

            } else if (f.getAnnouncement().getId() == announcement.getId()) {
                isTrue = false;
                favoriteRepository.delete(f);
                return SimpleResponse
                        .builder()
                        .httpStatus(HttpStatus.OK)
                        .message(String.format("Announcement with id: " + announcementId + " was deleted from your favorites."))
                        .build();
            }
        }
        if (isTrue = true) {
            favorite.setAnnouncement(announcement);
            favorite.setUser(user);
            favoriteRepository.save(favorite);
            return SimpleResponse
                    .builder()
                    .httpStatus(HttpStatus.OK)
                    .message(String.format("Announcement with id: " + announcementId + " was added to your favorites!"))
                    .build();
        }
        return null;
    }
}