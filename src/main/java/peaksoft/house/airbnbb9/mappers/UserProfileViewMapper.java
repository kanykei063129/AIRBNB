package peaksoft.house.airbnbb9.mappers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import peaksoft.house.airbnbb9.dto.response.UserAnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.UserBookingsResponse;
import peaksoft.house.airbnbb9.dto.response.UserProfileResponse;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.Booking;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.Position;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserProfileViewMapper {

    private final AnnouncementViewMapper announcementViewMapper;

    public UserAnnouncementResponse announcementToAnnouncementsResponse(Announcement announcement) {
        if (announcement == null) {
            return null;
        }
        log.info("Converting Announcement to UserAnnouncementResponse for announcement ID: " + announcement.getId());

        UserAnnouncementResponse announcementsResponse = new UserAnnouncementResponse();
        announcementsResponse.setId(announcement.getId());
        announcementsResponse.setImages(announcement.getImages());
        announcementsResponse.setHouseType(announcement.getHouseType());
        announcementsResponse.setPrice(announcement.getPrice());
        announcementsResponse.setRating(announcementViewMapper.calculateRating());
        announcementsResponse.setTitle(announcement.getTitle());
        announcementsResponse.setDescription(announcement.getDescription());
        announcementsResponse.setAddress(announcement.getAddress());
        announcementsResponse.setMaxGuests(announcement.getMaxGuests());
        announcementsResponse.setStatus(announcement.getStatus());
        announcementsResponse.setRegion(announcement.getRegion());
        announcementsResponse.setMessagesFromAdmin(announcement.getMessageFromAdmin());
        announcementsResponse.setIsBlocked(announcementViewMapper.isBlocked());
        announcementsResponse.setBookingsCountAnnouncement(announcement.getBookings().size());

        log.info("Converted Announcement to UserAnnouncementResponse successfully for announcement ID: " + announcement.getId());
        return announcementsResponse;
    }

    public List<UserAnnouncementResponse> listUserAnnouncements(List<Announcement> announcements) {
        List<UserAnnouncementResponse> responses = new ArrayList<>();

        log.info("Converting a list of Announcements to UserAnnouncementResponses");

        for (Announcement announcement : announcements) {
            if (announcement.getPosition().equals(Position.ACCEPTED) || announcement.getPosition().equals(Position.BLOCK)) {
                responses.add(announcementToAnnouncementsResponse(announcement));
            }
        }

        log.info("Converted the list of Accepted Announcements to UserAnnouncementResponses successfully");
        return responses;
    }

    public UserProfileResponse entityToDto(User user) {
        if (user == null) {
            return null;
        }
        log.info("Converting User entity to UserProfileResponse for user ID: " + user.getId());

        UserProfileResponse response = new UserProfileResponse();
        response.setImage(user.getImage());
        response.setName(user.getFullName());
        response.setContact(user.getEmail());
        response.setMessageFromAdmin(user.getMessagesFromAdmin());
        response.setBookings(listUserBookings(user.getBookings()));
        response.setAnnouncements(listUserAnnouncements(user.getAnnouncements()));
        response.setModerations(listUserAnnouncementsModeration(user.getAnnouncements()));

        log.info("Converted User entity to UserProfileResponse successfully for user ID: " + user.getId());
        return response;
    }

    public List<UserBookingsResponse> listUserBookings(List<Booking> bookings) {
        List<UserBookingsResponse> responses = new ArrayList<>();

        log.info("Converting a list of Bookings to UserBookingsResponses");

        for (Booking booking : bookings) {
            responses.add(bookingsToBookingResponse(booking));
        }
        log.info("Converted the list of Bookings to UserBookingsResponses successfully");
        return responses;
    }

    public UserBookingsResponse bookingsToBookingResponse(Booking booking) {
        if (booking == null) {
            return null;
        }
        log.info("Converting Booking to UserBookingsResponse for booking ID: " + booking.getId());

        UserBookingsResponse bookingsResponse = new UserBookingsResponse();
        bookingsResponse.setAnnouncementId(booking.getAnnouncement().getId());
        bookingsResponse.setImages(booking.getAnnouncement().getImages());
        bookingsResponse.setHouseType(booking.getAnnouncement().getHouseType());
        bookingsResponse.setPrice(booking.getAnnouncement().getPrice());
        bookingsResponse.setRegion(booking.getAnnouncement().getRegion());
        bookingsResponse.setRating(announcementViewMapper.calculateRating());
        bookingsResponse.setTitle(booking.getAnnouncement().getTitle());
        bookingsResponse.setDescription(booking.getAnnouncement().getDescription());
        bookingsResponse.setAddress(booking.getAnnouncement().getAddress());
        bookingsResponse.setMaxGuests(booking.getAnnouncement().getMaxGuests());
        bookingsResponse.setStatus(booking.getAnnouncement().getStatus());
        bookingsResponse.setCheckIn(booking.getCheckIn().format(DateTimeFormatter.ISO_LOCAL_DATE));
        bookingsResponse.setCheckOut(booking.getCheckOut().format(DateTimeFormatter.ISO_LOCAL_DATE));

        log.info("Converted Booking to UserBookingsResponse successfully for booking ID: " + booking.getId());
        return bookingsResponse;
    }

    public UserAnnouncementResponse announcementToAnnouncementsResponseModeration(Announcement announcement) {
        if (announcement == null) {
            return null;
        }
        log.info("Converting Announcement to UserAnnouncementResponse for announcement ID: " + announcement.getId());

        UserAnnouncementResponse announcementsResponse = new UserAnnouncementResponse();
        announcementsResponse.setId(announcement.getId());
        announcementsResponse.setImages(announcement.getImages());
        announcementsResponse.setHouseType(announcement.getHouseType());
        announcementsResponse.setRegion(announcement.getRegion());
        announcementsResponse.setPrice(announcement.getPrice());
        announcementsResponse.setRating(announcementViewMapper.calculateRating2());
        announcementsResponse.setTitle(announcement.getTitle());
        announcementsResponse.setDescription(announcement.getDescription());
        announcementsResponse.setAddress(announcement.getAddress());
        announcementsResponse.setMaxGuests(announcement.getMaxGuests());
        announcementsResponse.setStatus(announcement.getStatus());

        log.info("Converted Announcement to UserAnnouncementResponse successfully for announcement ID: " + announcement.getId());
        return announcementsResponse;
    }

    public List<UserAnnouncementResponse> listUserAnnouncementsModeration(List<Announcement> announcements) {
        List<UserAnnouncementResponse> responses = new ArrayList<>();

        log.info("Converting a list of Announcements to UserAnnouncementResponses");

        for (Announcement announcement : announcements) {
            if (announcement.getPosition() == Position.MODERATION) {
                responses.add(announcementToAnnouncementsResponseModeration(announcement));
            }
        }
        log.info("Converted the list of Announcements to UserAnnouncementResponses successfully");
        return responses;
    }
}