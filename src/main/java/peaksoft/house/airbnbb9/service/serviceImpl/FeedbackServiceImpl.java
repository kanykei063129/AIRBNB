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
import peaksoft.house.airbnbb9.dto.response.RatingCountResponse;
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
    public SimpleResponse saveFeedback(Long announcementId, FeedbackRequest request) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() ->
                new NotFoundException("Announcement whit id %s not found!".formatted(announcementId)));
        Feedback newFeedback = new Feedback();
        newFeedback.setImages(request.getImages());
        newFeedback.setRating(request.getRating());
        newFeedback.setComment(request.getComment());
        announcement.getFeedbacks().add(newFeedback);
        newFeedback.setAnnouncement(announcement);
        newFeedback.setCreateDate((LocalDate.now()));
        newFeedback.setUser(getCurrentUser());
        feedbackRepository.save(newFeedback);
        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message("successfully saved")
                .build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return userRepository.getUserByEmail(login).orElseThrow(() ->
                new ForbiddenException("An unregistered user cannot write comment for this announcement!"));
    }

    @Override
    public List<FeedbackResponse> getAllFeedback(Long announcementId) {
        announcementRepository.findById(announcementId).orElseThrow(() ->
                new NotFoundException("Announcement whit id %s not found!".formatted(announcementId)));
        return feedbackTemplate.getAllFeedback(announcementId);
    }

    public QuantityLikeAndDisLikeResponse likeAndDisLike(Long feedbackId, String likeOrDislike) throws AlreadyExistsException {
        User user = jwtService.getAuthentication();
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(() -> {
            String errorMessage = String.format("Feedback with %s not found", feedbackId);
            log.error(errorMessage);
            return new NotFoundException(errorMessage);
        });

        boolean isLike = likeOrDislike.equalsIgnoreCase("Like");
        boolean isDislike = likeOrDislike.equalsIgnoreCase("Dislike");

        Like existingLike = likeRepository.getLikeByUserIdAndFeedbackId(user.getId(), feedbackId).orElse(null);

        if (isLike || isDislike) {
            if (existingLike != null) {
                if ((isLike && !existingLike.getIsLiked()) || (isDislike && existingLike.getIsLiked())) {
                    existingLike.setIsLiked(isLike);
                    likeRepository.save(existingLike);
                    feedback.setLikeCount(Math.max(0, feedback.getLikeCount() + (isLike ? 1 : -1)));
                } else {
                    likeRepository.delete(existingLike);
                    feedback.setLikeCount(Math.max(0, feedback.getLikeCount() - 1));
                }
            } else {
                Like newLike = Like.builder().isLiked(isLike).feedback(feedback).user(user).build();
                feedback.getLikes().add(newLike);
                likeRepository.save(newLike);
                feedback.setLikeCount(feedback.getLikeCount() + (isLike ? 1 : 0));
                feedback.setDisLikeCount(feedback.getDisLikeCount() + (isDislike ? 1 : 0));
            }
        } else {
            log.warn("Invalid likeOrDislike value");
        }

        int dislikeCount = likeRepository.getCountLikeOrDislikeByFeedbackId(feedbackId, false);
        int likeCount = likeRepository.getCountLikeOrDislikeByFeedbackId(feedbackId, true);

        log.info(Integer.toString(dislikeCount));
        log.error(Integer.toString(likeCount));

        return QuantityLikeAndDisLikeResponse.builder()
                .disLikeCount(dislikeCount)
                .likeCount(likeCount)
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

    @Override
    public RatingCountResponse countRating(Long announcementId) {
        return feedbackTemplate.countRating(announcementId);
    }
}