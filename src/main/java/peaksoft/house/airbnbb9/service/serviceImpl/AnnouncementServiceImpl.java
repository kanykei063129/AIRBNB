package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.config.security.JwtService;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.*;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.*;
import peaksoft.house.airbnbb9.exception.BadRequestException;
import peaksoft.house.airbnbb9.exception.NotFoundException;
import peaksoft.house.airbnbb9.mappers.AnnouncementViewMapper;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.repository.FavoriteRepository;
import peaksoft.house.airbnbb9.repository.template.AnnouncementTemplate;
import peaksoft.house.airbnbb9.service.AnnouncementService;
import org.springframework.mail.javamail.JavaMailSender;


import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementTemplate announcementTemplate;
    private final JavaMailSender javaMailSender;
    private final AnnouncementViewMapper viewMapper;
    private final JwtService jwtService;
    private final FavoriteRepository favoriteRepository;

    @Override
    public SimpleResponse updateAnnouncement(Long announcementId, AnnouncementRequest request) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() ->
                new NotFoundException("Announcement with id " + announcementId + " not found!"));
        viewMapper.updateAnnouncement(announcement, request);
        checkAdField(request, announcement);
        announcementRepository.save(announcement);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(String.format("Announcement with id " + announcementId + ", successfully updated."))
                .build();
    }

    private void checkAdField(AnnouncementRequest request, Announcement announcement) {

        if (request.getImages().size() <= 4) {
            announcement.setImages(request.getImages());
        } else {
            throw new BadRequestException(" You can upload up to 4 photos !");
        }

        if (request.getMaxGuests() <= 0) {
            throw new BadRequestException("The number of guests cannot be negative and equal to zero!");
        }
        if (request.getPrice() <= 0) {
            throw new BadRequestException("The ad price cannot be negative or zero!");
        }
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncements() {
        return announcementTemplate.getAllAnnouncements();
    }

    protected Announcement getAnnouncementById(Long announcementId) {
        log.info("Getting Announcement with ID: " + announcementId);

        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() ->
                new NotFoundException("Announcement with id " + announcementId + " not found!"));

        log.info("Announcement with ID " + announcementId + " has been retrieved.");

        return announcement;
    }

    public SimpleResponse deleteByIdAnnouncement(Long announcementId) {
        log.info("Deleting Announcement with ID: " + announcementId);

        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() -> new NotFoundException("Announcement with id: " + announcementId + " does not exist!"));
        announcementRepository.delete(announcement);

        log.info("Announcement with ID: " + announcementId + " deleted.");
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(String.format("Announcement with id: " + announcementId + " deleted..."))
                .build();
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilter(Status status, HouseType houseType, String rating, String price) {
        return announcementTemplate.getAllAnnouncementsFilter(status, houseType, rating, price);
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilterVendor(Region region, HouseType houseType, String rating, String price) {
        return announcementTemplate.getAllAnnouncementsFilterVendor(region, houseType, rating, price);
    }

    @Override
    public PaginationAnnouncementResponse getAllAnnouncementsModerationAndPagination(int currentPage, int pageSize) {
        return announcementTemplate.getAllAnnouncementsModerationAndPagination(currentPage, pageSize);
    }

    @Override
    public LatestAnnouncementResponse getLatestAnnouncement() {
        return announcementTemplate.getLatestAnnouncement();
    }

    @Override
    public List<PopularHouseResponse> getPopularHouses() {
        return announcementTemplate.getPopularHouses();
    }

    public PopularApartmentResponse getPopularApartment() {
        return announcementTemplate.getPopularApartment();
    }

    @Override
    public GlobalSearchResponse search(String word, boolean isNearby, Double latitude, Double longitude) {
        return announcementTemplate.search(word, isNearby, latitude, longitude);
    }

    @Override
    public SimpleResponse processAnnouncement(Long announcementId, String message, String messageFromAdminToUser) throws MessagingException {
        log.info("Processing announcement with id={} and action={}", announcementId, message);
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new NotFoundException("Announcement with id: " + announcementId + " does not exist!"));
        if (announcement.getPosition() == Position.MODERATION) {
            if (message.equalsIgnoreCase("accept")) {
                announcement.setPosition(Position.ACCEPTED);
                announcementRepository.save(announcement);
                log.info("Announcement with id={} accepted successfully", announcementId);
                return SimpleResponse.builder().
                        message("Announcement accepted successfully").httpStatus(HttpStatus.OK).
                        build();
            } else if (message.equalsIgnoreCase("reject")) {
                announcement.setPosition(Position.REJECT);
                announcementRepository.save(announcement);
                sendRejectionMessageToUser(announcement, messageFromAdminToUser);
                log.info("Announcement with id={} rejected successfully", announcementId);
                return SimpleResponse.builder().
                        message("Announcement rejected successfully").httpStatus(HttpStatus.OK).
                        build();
            } else if (message.equalsIgnoreCase("delete")) {
                announcementRepository.delete(announcement);
                log.info("Announcement with id={} deleted successfully", announcementId);
                return SimpleResponse.builder().
                        message("Announcement deleted successfully.").httpStatus(HttpStatus.OK).
                        build();
            } else if (message.isEmpty()) {
                return SimpleResponse.builder().
                        message("Invalid action. Use 'accept', 'reject', or 'delete'.").httpStatus(HttpStatus.OK).
                        build();
            } else {
                log.info("Invalid action '{}' for announcement with id={}", message, announcementId);
                return SimpleResponse.builder().
                        message("Invalid action. Use 'accept', 'reject', or 'delete'.").httpStatus(HttpStatus.OK).
                        build();
            }
        } else {
            return SimpleResponse.builder().
                    message("Invalid position for processing.").httpStatus(HttpStatus.OK).
                    build();
        }
    }

    private void sendRejectionMessageToUser(Announcement announcement, String messageFromAdminToUser) throws MessagingException {
        User user = announcement.getUser();
        log.info("Sending rejection message to user with email={}", user.getEmail());

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(user.getEmail());
        helper.setFrom("adiibrahimov@gmail.com");
        helper.setSubject("AirBnb b9");
        helper.setText("Dear " + user.getFullName() + "\n" +
                "your application with id: " + announcement.getId() + " was rejected because:" + messageFromAdminToUser
        );
        javaMailSender.send(message);
        log.info("Rejection message sent to user with email={}", user.getEmail());
    }

    @Override
    public AnnouncementResponse getApplicationById(Long applicationId) {
        return announcementTemplate.getApplicationById(applicationId);
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilters(HouseType houseType, String rating, PriceType price) {
        return announcementTemplate.getAllAnnouncementsFilters(houseType, rating, price);
    }

    @Override
    public PaginationAnnouncementResponse pagination(Integer page, Integer size) {
        return announcementTemplate.pagination(page, size);
    }

    @Override
    public AnnouncementsResponseProfile getAnnouncementsByIdProfile(Long announcementId) {
        return announcementTemplate.getAnnouncementByIdProfile(announcementId);
    }

    @Override
    public SimpleResponse blockedAnnouncementById(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new NotFoundException("Announcement with id: " + announcementId + " does not exist!"));
        if (announcement.getPosition() == Position.ACCEPTED) {
            announcement.setPosition(Position.BLOCK);
            return SimpleResponse.builder().
                    message("Announcement Blocked!!!").httpStatus(HttpStatus.OK).
                    build();
        }
        return SimpleResponse.builder().
                message("Announcement item not accepted or pending !!!").httpStatus(HttpStatus.OK).
                build();
    }
}