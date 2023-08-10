package peaksoft.house.airbnbb9.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import peaksoft.house.airbnbb9.dto.response.UserAnnouncementResponse;
import peaksoft.house.airbnbb9.dto.response.UserBookingsResponse;
import peaksoft.house.airbnbb9.dto.response.UserProfileResponse;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.Booking;
import peaksoft.house.airbnbb9.entity.User;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserProfileViewMapper {

    private final AnnouncementViewMapper announcementViewMapper;

    public UserAnnouncementResponse announcementToAnnouncementsResponse(Announcement announcement) {
        if (announcement == null) {
            return null;
        }
        UserAnnouncementResponse announcementsResponse = new UserAnnouncementResponse();
        announcementsResponse.setId(announcement.getId());
        announcementsResponse.setImage(announcement.getImages().get(0));
        announcementsResponse.setHouseType(announcement.getHouseType());
        announcementsResponse.setPrice(announcement.getPrice());
        announcementsResponse.setRating(announcementViewMapper.calculateRating1());
        announcementsResponse.setTitle(announcement.getTitle());
        announcementsResponse.setDescription(announcement.getDescription());
        announcementsResponse.setAddress(announcement.getAddress());
        announcementsResponse.setMaxGuests(announcement.getMaxGuests());
        announcementsResponse.setStatus(announcement.getStatus());
        announcementsResponse.setMessagesFromAdmin(announcement.getMessageFromAdmin());
        announcementsResponse.setBookingsCountAnnouncement(announcement.getBookings().size());
        return announcementsResponse;
    }

    public List<UserAnnouncementResponse> listUserAnnouncements(List<Announcement> announcements) {
        List<UserAnnouncementResponse> responses = new ArrayList<>();
        for (Announcement announcement : announcements) {
            responses.add(announcementToAnnouncementsResponse(announcement));
        }
        return responses;
    }

    public UserProfileResponse entityToDto(User user) {
        if (user == null) {
            return null;
        }
        UserProfileResponse response = new UserProfileResponse();
        response.setImage(user.getImage());
        response.setName(user.getFullName());
        response.setContact(user.getEmail());
        response.setMessageFromAdmin(user.getMessagesFromAdmin());
        response.setBookings(listUserBookings(user.getBookings()));
        response.setAnnouncements(listUserAnnouncements(user.getAnnouncements()));
        return response;
    }

    public List<UserBookingsResponse> listUserBookings(List<Booking> bookings) {
        List<UserBookingsResponse> responses = new ArrayList<>();
        for (Booking booking : bookings) {
            responses.add(bookingsToBookingResponse(booking));
        }
        return responses;
    }

    public UserBookingsResponse bookingsToBookingResponse(Booking booking) {
        if (booking == null) {
            return null;
        }
        UserBookingsResponse bookingsResponse = new UserBookingsResponse();
        bookingsResponse.setAnnouncementId(booking.getAnnouncement().getId());
        bookingsResponse.setImage(booking.getAnnouncement().getImages().get(0));
        bookingsResponse.setRating(announcementViewMapper.calculateRating());
        bookingsResponse.setTitle(booking.getAnnouncement().getTitle());
        bookingsResponse.setAddress(booking.getAnnouncement().getAddress());
        bookingsResponse.setMaxGuests(booking.getAnnouncement().getMaxGuests());
        bookingsResponse.setCheckIn(booking.getCheckIn().format(DateTimeFormatter.ISO_LOCAL_DATE));
        bookingsResponse.setCheckOut(booking.getCheckOut().format(DateTimeFormatter.ISO_LOCAL_DATE));
        return bookingsResponse;
    }
}