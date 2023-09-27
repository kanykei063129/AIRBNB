package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import peaksoft.house.airbnbb9.config.security.JwtService;
import peaksoft.house.airbnbb9.dto.request.AnnouncementRequest;
import peaksoft.house.airbnbb9.dto.response.*;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.Booking;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.Position;
import peaksoft.house.airbnbb9.exception.BadRequestException;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.repository.BookingRepository;
import peaksoft.house.airbnbb9.repository.template.AnnouncementTemplate;
import peaksoft.house.airbnbb9.service.AnnouncementVendorService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AnnouncementVendorServiceImpl implements AnnouncementVendorService {

    private final BookingRepository bookingRepository;
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementTemplate announcementTemplate;
    private final JwtService jwtService;
    @Value("${google.api.key}")
    private String google_api_key;

    private RestTemplate restTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

    public peaksoft.house.airbnbb9.dto.response.LatLng getGeoCoordinateForAddress(String address) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + google_api_key;
        GeocodingResponse response = restTemplate().getForObject(url, GeocodingResponse.class);
        if (response != null && response.getResults().size() > 0) {
            GeocodingResult result = response.getResults().get(0);
            return result.getGeometry().getLocation();
        } else throw new BadRequestException("Address not found");
    }

    @Override
    public SimpleResponse submitAnAd(AnnouncementRequest announcementRequest) {
        LatLng coordinate = getGeoCoordinateForAddress(announcementRequest.getAddress());
        double latitude = coordinate.getLat();
        double longitude = coordinate.getLng();
        log.info("Submitting an advertisement with title: {}", announcementRequest.getTitle());

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
        announcement.setPosition(Position.MODERATION);
        announcement.setLatitude(latitude);
        announcement.setLongitude(longitude);
        announcement.setCard(true);
        try {
            announcementRepository.save(announcement);
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(() -> {
                announcement.setCard(false);
                announcementRepository.save(announcement);
            }, 1, TimeUnit.HOURS);
            scheduler.shutdown();
            log.info("Advertisement with title: {} successfully submitted", announcementRequest.getTitle());
        } catch (Exception e) {
            log.error("Error while saving the advertisement: {}", e.getMessage(), e);
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

    @Override
    public GetAnnouncementResponse getAnnouncementById(Long announcementId) {
        User user = jwtService.getAuthentication();
        GetAnnouncementResponse announcement = announcementTemplate.getAnnouncementById(announcementId);
        List<Booking> all = bookingRepository.findAll();
        for (Booking b : all) {
            if (b.getAnnouncement().getId().equals(announcementId) && b.getUser().equals(user)) {
                announcement.setBooked(true);
                if(b.getCheckOut().isBefore(LocalDate.now())){
                    announcement.setLived(true);
                }
                log.info("Get announcement by id");
                return announcement;
            }
        }
        return announcement;
    }

}