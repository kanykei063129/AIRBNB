package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.request.BookRequest;
import peaksoft.house.airbnbb9.dto.request.UpdateBookRequest;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.Booking;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.Position;
import peaksoft.house.airbnbb9.enums.Role;
import peaksoft.house.airbnbb9.exception.BadRequestException;
import peaksoft.house.airbnbb9.exception.ForbiddenException;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.repository.BookingRepository;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.service.BookingService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final AnnouncementRepository announcementRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    private User getAuthenticatedRoleUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userRepository.getUserByEmail(login).orElseThrow(() ->
                new ForbiddenException("An unregistered user cannot write comment for this announcement!"));
        if (!user.getRole().equals(Role.USER)) {
            throw new ForbiddenException("You are not user!");
        }
        return user;
    }

    @Override
    public Map<String, String> requestToBook(BookRequest request) {
        User user = getAuthenticatedRoleUser();
        Announcement announcement = announcementRepository.findById(request.getAnnouncementId()).orElseThrow(BadRequestException::new);

        if (request.getAnnouncementId() == null || request.getCheckIn() == null || request.getCheckOut() == null) {
            throw new BadRequestException("Incomplete information!!!");
        }

        if (request.getCheckIn().isAfter(request.getCheckOut()) ||
                request.getCheckIn().equals(request.getCheckOut()) || request.getCheckIn().isBefore(LocalDate.now())) {
            throw new BadRequestException("Dates are incorrect!!!");
        }
        findTakenDates(request.getCheckIn(), request.getCheckOut(), announcement.getBlockedDates(), announcement.getBlockedDatesByUser());

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setAnnouncement(announcement);
        booking.setCheckIn((request.getCheckIn()));
        booking.setCheckOut((request.getCheckOut()));
        booking.setPricePerDay(BigDecimal.valueOf(announcement.getPrice()));
        booking.setDate(LocalDate.now());
        bookingRepository.save(booking);
        return Map.of("massage", "Booking request sent");
    }

    @Override
    public Map<String, String> updateRequestToBook(UpdateBookRequest request) {
        if (request.getCheckIn().isAfter(request.getCheckOut()) ||
                request.getCheckIn().equals(request.getCheckOut()) ||
                request.getCheckIn().isBefore(LocalDate.now())) {
            throw new BadRequestException("Date is incorrect!");
        }

        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow(BadRequestException::new);
        Announcement announcement = announcementRepository.findById(request.getAnnouncementId()).orElseThrow(BadRequestException::new);
        User user = getAuthenticatedRoleUser();

        if (!booking.getUser().getId().equals(user.getId()) ||
                !booking.getAnnouncement().getId().equals(request.getAnnouncementId())) {
            throw new ForbiddenException("incorrect id");
        }

        findTakenDates(booking.getCheckIn(), booking.getCheckOut(), announcement.getBlockedDates(), announcement.getBlockedDatesByUser());

        if (booking.getPosition().equals(Position.ACCEPTED)) {
            booking.setDate(LocalDate.now());
            announcement.releaseTakenDates(booking.getCheckIn(), booking.getCheckOut());
        }
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        bookingRepository.save(booking);
        announcementRepository.save(announcement);
        return Map.of("massage", "The dates has been updated!");
    }

    private void findTakenDates(LocalDate checkIn, LocalDate checkOut, List<LocalDate> takenDates, List<LocalDate> takenDatesByUser) {
        takenDates.addAll(takenDatesByUser);
        while (checkIn.isBefore(checkOut)) {
            if (takenDates.contains(checkIn)) {
                throw new BadRequestException("Intermediate dates of your booking are busy!");
            }
            checkIn = checkIn.plusDays(1L);
        }
        if (takenDates.contains(checkOut)) {
            throw new BadRequestException("Intermediate dates of your booking are busy!");
        }
    }
}
