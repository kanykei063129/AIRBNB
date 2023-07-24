package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.SimpleResponse;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.AllAnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.Feedback;
import peaksoft.house.airbnbb9.exceptoin.NotFoundException;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.service.AnnouncementService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;


    @Override
    public List<AnnouncementResponse> getByIdUser(Long userId) {
        return userRepository.getUserById(userId).orElseThrow(() ->
                new NotFoundException("User with id: " + userId + " is no exist!"));
    }

    @Override
    public AllAnnouncementResponse getByIdAnnouncement(Long id) {
            Announcement announcement = announcementRepository.findById(id).orElseThrow(() -> new NotFoundException("Announcement with id: " + id + " is no exist!"));
            List<Feedback> feedbacks = announcementRepository.getAllAnnouncementFeedback(id);
            return AllAnnouncementResponse.builder()
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
                    .isFavorite(feedbacks.size())
                    .build();
        }

    @Override
    public AnnouncementResponse updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() -> new NotFoundException(" Announcement with id: " + announcementId + " is no exist!"));
        announcement.setHouseType(announcementRequest.houseType());
        announcement.setImages(announcementRequest.images());
        announcement.setPrice(announcementRequest.price());
        announcement.setRegion(announcementRequest.region());
        announcement.setAddress(announcementRequest.address());
        announcement.setDescription(announcementRequest.description());
        announcement.setStatus(announcementRequest.status());
        announcement.setTitle(announcementRequest.title());
        announcement.setMaxGuests(announcementRequest.maxGuests());
        announcement.setProvince(announcementRequest.province());
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
        return announcementRepository.getAll();
    }

    @Override
    public SimpleResponse deleteByIdAnnouncement(Long id) {
        if (announcementRepository.existsById(id)) {
            announcementRepository.deleteById(id);
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Successfully deleted...")
                    .build();
        } else throw new NoSuchElementException(String.format("Announcement with id:%s does not exist", id));
    }
}
