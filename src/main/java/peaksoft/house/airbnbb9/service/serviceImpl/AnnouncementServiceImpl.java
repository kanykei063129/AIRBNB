package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.*;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.*;
import peaksoft.house.airbnbb9.exceptoin.NotFoundException;
import peaksoft.house.airbnbb9.mappers.AnnouncementViewMapper;
import peaksoft.house.airbnbb9.mappers.BookingViewMapper;
import peaksoft.house.airbnbb9.mappers.FeedbackViewMapper;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.repository.template.AnnouncementTemplate;
import peaksoft.house.airbnbb9.service.AnnouncementService;
import org.springframework.mail.javamail.JavaMailSender;


import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementTemplate announcementTemplate;
    private final JavaMailSender javaMailSender;
    private final AnnouncementViewMapper viewMapper;
    private final BookingViewMapper bookingViewMapper;
    private final FeedbackViewMapper feedbackViewMapper;

    @Override
    public AnnouncementResponse updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() ->
                new NotFoundException(" Announcement with id: " + announcementId + " is no exist!"));
        announcement.setHouseType(announcementRequest.getHouseType());
        announcement.setImages(announcementRequest.getImages());
        announcement.setPrice(announcementRequest.getPrice());
        announcement.setRegion(announcementRequest.getRegion());
        announcement.setAddress(announcementRequest.getAddress());
        announcement.setDescription(announcementRequest.getDescription());
        announcement.setStatus(announcementRequest.getStatus());
        announcement.setTitle(announcementRequest.getTitle());
        announcement.setMaxGuests(announcementRequest.getMaxGuests());
        announcement.setProvince(announcementRequest.getProvince());
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
        return announcementTemplate.getAllAnnouncements();
    }
    protected Announcement getAnnouncementById(Long announcementId) {
        return announcementRepository.findById(announcementId).orElseThrow(() ->
                new NotFoundException("Announcement with id " + announcementId + " not found!"));
    }

    @Override
    public AnnouncementInnerPageResponse getAnnouncementDetails(Long announcementId) {
        Announcement announcementById = getAnnouncementById(announcementId);
        announcementRepository.save(announcementById);
        return getAnnouncementInnerPageResponse(announcementById);
    }
    private AnnouncementInnerPageResponse getAnnouncementInnerPageResponse(Announcement announcement) {
        AnnouncementInnerPageResponse response = viewMapper.entityToDtoConverting(announcement);
        response.setAnnouncementBookings(bookingViewMapper.viewBooked(announcement.getBookings()));
        response.setFeedbackResponses(feedbackViewMapper.viewFeedback(announcement.getFeedbacks()));
        return response;
    }


    public SimpleResponse deleteByIdAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() -> new NotFoundException("Announcement with id: " + announcementId + " does not exist!"));
        announcementRepository.delete(announcement);
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
    public GlobalSearchResponse search (String word) {
        return announcementTemplate.search(word);
    }

    @Override
    public SimpleResponse processAnnouncement(Long announcementId, String message) throws MessagingException {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new NotFoundException("Announcement with id: " + announcementId + " does not exist!"));
        if (announcement.getPosition() == Position.MODERATION) {
            if (message.equalsIgnoreCase("accept")) {
                announcement.setPosition(Position.ACCEPTED);
                announcementRepository.save(announcement);
                return SimpleResponse.builder().
                        message("Announcement accepted successfully").httpStatus(HttpStatus.OK).
                        build();
            } else if (message.equalsIgnoreCase("reject")) {
                announcement.setPosition(Position.REJECT);
                announcementRepository.save(announcement);
                sendRejectionMessageToUser(announcement);
                return SimpleResponse.builder().
                        message("Announcement rejected successfully").httpStatus(HttpStatus.OK).
                        build();
            } else if (message.equalsIgnoreCase("delete")) {
                announcementRepository.delete(announcement);
                return SimpleResponse.builder().
                        message("Announcement deleted successfully.").httpStatus(HttpStatus.OK).
                        build();
            } else {
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

    private void sendRejectionMessageToUser(Announcement announcement) throws MessagingException {
        User user = announcement.getUser();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
        helper.setTo(user.getEmail());
        helper.setFrom("adiibrahimov@gmail.com");
        helper.setSubject("Your Announcement Status");
        helper.setText("Dear " + user.getFullName() + "\n" +
                "                \"We regret to inform you that your announcement with ID \" + announcement.getId() +\n" +
                "                \" has been rejected. Please review the guidelines and resubmit if necessary.\\n\\n\" +\n" +
                "                \"Thank you,\\n\" +\n" +
                "                \"Your Announcement Platform Team");
        javaMailSender.send(message);
    }


    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilters(HouseType houseType, String rating, PriceType price) {
        return announcementTemplate.getAllAnnouncementsFilters(houseType,rating,price);
    }

    @Override
    public PaginationAnnouncementResponse pagination(Integer page, Integer size) {
        return announcementTemplate.pagination(page,size);
    }
}