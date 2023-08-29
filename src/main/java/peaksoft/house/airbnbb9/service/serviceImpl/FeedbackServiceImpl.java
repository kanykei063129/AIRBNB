package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.config.security.JwtService;
import peaksoft.house.airbnbb9.dto.request.FeedbackRequest;
import peaksoft.house.airbnbb9.dto.request.FeedbackUpdateRequest;
import peaksoft.house.airbnbb9.dto.response.FeedbackResponse;
import peaksoft.house.airbnbb9.dto.response.QuantityLikeAndDisLikeResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.Feedback;
import peaksoft.house.airbnbb9.entity.Like;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.exception.AlreadyExistsException;
import peaksoft.house.airbnbb9.exception.ForbiddenException;
import peaksoft.house.airbnbb9.exception.NotFoundException;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.repository.FeedbackRepository;
import peaksoft.house.airbnbb9.repository.LikeRepository;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.repository.template.FeedbackTemplate;
import peaksoft.house.airbnbb9.service.FeedbackService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackTemplate feedbackTemplate;
    private final LikeRepository likeRepository;
    private final JwtService jwtService;

    @Override
    public FeedbackResponse saveFeedback(Long announcementId, FeedbackRequest request) {
        Announcement announcement = getFindByAnnouncementId(announcementId);
        Feedback newFeedback = new Feedback();
        newFeedback.setImages(request.getImages());
        newFeedback.setRating(request.getRating());
        newFeedback.setComment(request.getComment());
        announcement.getFeedbacks().add(newFeedback);
        newFeedback.setAnnouncement(announcement);
        newFeedback.setCreateDate((LocalDate.now()));
        newFeedback.setUser(getCurrentUser());
        feedbackRepository.save(newFeedback);
        return getFeedbackResponse(newFeedback);
    }

    private FeedbackResponse getFeedbackResponse(Feedback feedback) {
        User user = getCurrentUser();
        FeedbackResponse feedbackResponse = new FeedbackResponse();
        feedbackResponse.setId(feedback.getId());
        feedbackResponse.setFeedbackUserImage(user.getImage());
        feedbackResponse.setFeedbackUserFullName(user.getFullName());
        feedbackResponse.setRating(feedback.getRating());
        feedbackResponse.setImages(feedback.getImages());
        feedbackResponse.setComment(feedback.getComment());
        feedbackResponse.setCreatedAt((feedback.getCreateDate()));
        feedbackResponse.setLikeCount(feedback.getLikeCount());
        feedbackResponse.setDisLikeCount(feedback.getDisLikeCount());
        return feedbackResponse;
    }

    private Announcement getFindByAnnouncementId(Long id) {
        return announcementRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Announcement whit id = " + id + " not found!"));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return userRepository.getUserByEmail(login).orElseThrow(() ->
                new ForbiddenException("An unregistered user cannot write comment for this announcement!"));
    }

    @Override
    public List<FeedbackResponse> getAllFeedback(Long announcementId) {
        return feedbackTemplate.getAllFeedback(announcementId);
    }

    @Override
    public QuantityLikeAndDisLikeResponse likeAndDisLike(Long feedbackId, String likeOrDislike) throws AlreadyExistsException {
        User user = jwtService.getAuthentication();
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(
                () -> {
                    log.error("Feedback with %s not found".formatted(feedbackId));
                    return new NotFoundException("Feedback with %s not found".formatted(feedbackId));
                }
        );
        List<Like> likeByFeedbackId = likeRepository.getLikeByFeedbackId(feedbackId);

        if (likeOrDislike.equalsIgnoreCase("Like")) {
            boolean likeStatus = false;
            for (Like l : likeByFeedbackId) {
                if (l.getUser().getId().equals(user.getId())) {
                    likeStatus = true;
                    break;
                }
            }
            if (!likeStatus) {
                Like like = Like
                        .builder()
                        .isLiked(true)
                        .feedback(feedback)
                        .user(user)
                        .build();
                feedback.getLikes().add(like);
                int likeOrDislikeCount = likeRepository.getCountLikeOrDislikeByFeedbackId(feedbackId, true);
                feedback.setLikeCount(1 + likeOrDislikeCount);
                likeRepository.save(like);
            } else {
                Like like = likeRepository.getLikeByUserIdAndFeedbackId(user.getId(), feedbackId).orElseThrow(
                        () -> {
                            log.error("Like not found");
                            return new NotFoundException("Like not found");
                        });
                like.getFeedback().getLikes().remove(like);
                likeRepository.delete(like);
                int likeOrDislikeCount = likeRepository.getCountLikeOrDislikeByFeedbackId(feedbackId, true);
                feedback.setLikeCount(likeOrDislikeCount);
            }
        } else if (likeOrDislike.equalsIgnoreCase("Dislike")) {

            boolean likeStatus = false;
            for (Like l : likeByFeedbackId) {
                if (l.getUser().getId().equals(user.getId())) {
                    likeStatus = true;
                    break;
                }
            }
            if (!likeStatus) {
                Like like = Like
                        .builder()
                        .isLiked(false)
                        .feedback(feedback)
                        .user(user)
                        .build();
                feedback.getLikes().add(like);
                int likeOrDislikeCount = likeRepository.getCountLikeOrDislikeByFeedbackId(feedbackId, false);
                feedback.setDisLikeCount(1 + likeOrDislikeCount);
                likeRepository.save(like);
            } else {
                Like like = likeRepository.getLikeByUserIdAndFeedbackId(user.getId(), feedbackId).orElseThrow(
                        () -> {
                            log.error("Like not found");
                            return new NotFoundException("Like not found");
                        });
                like.getFeedback().getLikes().remove(like);
                likeRepository.delete(like);
                int likeOrDislikeCount = likeRepository.getCountLikeOrDislikeByFeedbackId(feedbackId, false);
                feedback.setLikeCount(likeOrDislikeCount - 1);

            }
        }
        feedbackRepository.save(feedback);
        int dislikeCount = likeRepository.getCountLikeOrDislikeByFeedbackId(feedbackId, false);
        int likeCount = likeRepository.getCountLikeOrDislikeByFeedbackId(feedbackId, true);
        log.info(Integer.toString(dislikeCount));
        log.error(Integer.toString(likeCount));

        return QuantityLikeAndDisLikeResponse
                .builder()
                .disLikeCount(likeRepository.getCountLikeOrDislikeByFeedbackId(feedbackId, false))
                .likeCount(likeRepository.getCountLikeOrDislikeByFeedbackId(feedbackId, true))
                .build();
    }

    @Override
    public SimpleResponse updateFeedback(Long feedbackId, FeedbackUpdateRequest feedbackUpdateRequest) {
        User user = jwtService.getAuthentication();
        if (!feedbackRepository.existsFeedbackById(feedbackId)) {
            throw new NotFoundException("Feedback with id: %s not found".formatted(feedbackId));
        }
        Feedback feedback = feedbackRepository.getFeedbackByIdAndUserId(feedbackId, user.getId()).orElseThrow(
                () -> {
                    log.error("This review is id: %s was not found.".formatted(feedbackId));
                    return new ForbiddenException("This review is id: %s was not found.".formatted(feedbackId));
                }
        );
        feedback.setComment(feedbackUpdateRequest.getComment());
        feedback.setImages(feedbackUpdateRequest.getImages());
        feedbackRepository.save(feedback);

        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully updated")
                .build();
    }

    @Override
    public SimpleResponse deleteFeedback(Long feedbackId) {
        User user = jwtService.getAuthentication();
        if (!feedbackRepository.existsFeedbackById(feedbackId)) {
            throw new NotFoundException("Feedback with id: %s not found".formatted(feedbackId));
        }
        Feedback feedback = feedbackRepository.getFeedbackByIdAndUserId(feedbackId, user.getId()).orElseThrow(
                () -> {
                    log.error("This review is id: %s was not found.".formatted(feedbackId));
                    return new ForbiddenException("This review is id: %s was not found.".formatted(feedbackId));
                }
        );
        feedbackRepository.delete(feedback);
        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully deleted")
                .build();
    }
}