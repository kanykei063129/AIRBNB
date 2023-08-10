package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.config.security.JwtService;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.Position;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.exceptoin.BadRequestException;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.service.AnnouncementVendorService;

import java.time.LocalDate;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AnnouncementVendorServiceImpl implements AnnouncementVendorService {

    private final AnnouncementRepository announcementRepository;
    private final JwtService jwtService;

    @Override
    public SimpleResponse submitAnAd(AnnouncementRequest announcementRequest) {
        if (announcementRequest == null) {
            throw new BadRequestException("Announcement request cannot be empty!");
        }
        if (announcementRequest.getTitle() == null || announcementRequest.getTitle().isEmpty()) {
            throw new BadRequestException("Title cannot be empty!");
        }
        if (announcementRequest.getPrice() <= 0) {
            throw new BadRequestException("Price must be a positive number!");
        }
        User user = jwtService.getAuthentication();
        Announcement announcement = new Announcement();
        announcement.setImages(announcementRequest.getImages());
        announcement.setHouseType(announcementRequest.getHouseType());
        announcement.setMaxGuests(announcementRequest.getMaxGuests());
        announcement.setPrice(announcementRequest.getPrice());
        announcement.setTitle(announcementRequest.getTitle());
        announcement.setDescription(announcementRequest.getDescription());
        announcement.setRegion(announcementRequest.getRegion());
        announcement.setProvince(announcementRequest.getProvince());
        announcement.setAddress(announcementRequest.getAddress());
        announcement.setCreateDate(LocalDate.now());
        announcement.setUser(user);
        announcement.setStatus(Status.NOT_BOOKED);
        announcement.setPosition(Position.MODERATION);
        try {
            announcementRepository.save(announcement);
        } catch (Exception e) {
            return SimpleResponse
                    .builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("An error occurred while saving the advertisement!")
                    .build();
        }
        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message(String.format("announcement  with title: %s is successfully submit!", announcementRequest.getTitle()))
                .build();
    }
}