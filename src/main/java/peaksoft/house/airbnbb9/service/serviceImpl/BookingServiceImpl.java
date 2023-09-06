package peaksoft.house.airbnbb9.service.serviceImpl;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.request.BookRequest;
import peaksoft.house.airbnbb9.dto.request.UpdateBookRequest;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.Booking;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.Position;
import peaksoft.house.airbnbb9.enums.Role;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.exception.BadRequestException;
import peaksoft.house.airbnbb9.exception.ForbiddenException;
import peaksoft.house.airbnbb9.exception.NotFoundException;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.repository.BookingRepository;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.service.BookingService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
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
    public SimpleResponse requestToBook(BookRequest request) throws StripeException {
        User user = getAuthenticatedRoleUser();
        Announcement announcement = announcementRepository.findById(request.getAnnouncementId())
                .orElseThrow(() -> new NotFoundException("Announcement with id " + request.getAnnouncementId() + " not found!"));

        if (!announcement.getPosition().equals(Position.ACCEPTED)) {
            throw new BadRequestException("You cannot book this announcement!");
        }
        if (request.getAnnouncementId() == 0 || request.getCheckIn() == null || request.getCheckOut() == null || request.getAmount() <= 0) {
            throw new BadRequestException("Invalid or incomplete information!");
        }
        if (request.getCheckIn().isAfter(request.getCheckOut()) || request.getCheckIn().equals(request.getCheckOut())) {
            throw new BadRequestException("Check-in date must be before check-out date!");
        }
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setAnnouncement(announcement);
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        booking.setPricePerDay(BigDecimal.valueOf(announcement.getPrice()));
        booking.setPosition(Position.ACCEPTED);

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int) (request.getAmount() * 100)); // Сумма в центах
        chargeParams.put("currency", "USD");
        chargeParams.put("source", request.getToken());
        Charge charge = Charge.create(chargeParams);
        booking.setDate(LocalDate.now());
        announcement.setStatus(Status.BOOKED);
        bookingRepository.save(booking);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Booking successful! Customer: " + charge.getCustomer())
                .build();
    }

    @Override
    public SimpleResponse updateRequestToBook(UpdateBookRequest request) throws StripeException {
        if (request.getCheckIn().isAfter(request.getCheckOut()) ||
                request.getCheckIn().equals(request.getCheckOut())) {
            throw new BadRequestException("Date is incorrect!");
        }
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new NotFoundException("Booking with id " + request.getBookingId() + " not found!"));
        Announcement announcement = announcementRepository.findById(request.getAnnouncementId())
                .orElseThrow(() -> new NotFoundException("Announcement with id " + request.getAnnouncementId() + " not found!"));
        User user = getAuthenticatedRoleUser();
        if (!booking.getUser().getId().equals(user.getId()) || !booking.getAnnouncement().getId().equals(request.getAnnouncementId())) {
            throw new ForbiddenException("incorrect id");
        }
        if (booking.getPosition().equals(Position.ACCEPTED)) {
            booking.setDate(LocalDate.now());
        }
        booking.setPricePerDay(BigDecimal.valueOf(announcement.getPrice()));
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int) (request.getAmount() * 100)); // Сумма в центах
        chargeParams.put("currency", "USD");
        chargeParams.put("source", request.getToken());
        Charge charge = Charge.create(chargeParams);
        booking.setDate(LocalDate.now());
        bookingRepository.save(booking);
        announcementRepository.save(announcement);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("The dates has been updated! Customer: " + charge.getCustomer())
                .build();
    }
}
