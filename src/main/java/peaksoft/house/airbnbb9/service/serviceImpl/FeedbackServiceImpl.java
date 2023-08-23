package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.request.FeedbackRequest;
import peaksoft.house.airbnbb9.dto.response.FeedbackResponse;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.Feedback;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.exceptoin.ForbiddenException;
import peaksoft.house.airbnbb9.exceptoin.NotFoundException;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.repository.FeedbackRepository;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.service.FeedbackService;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
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
}
