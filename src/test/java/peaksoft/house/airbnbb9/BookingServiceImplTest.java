package peaksoft.house.airbnbb9;

import com.stripe.exception.StripeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import peaksoft.house.airbnbb9.dto.request.BookRequest;
import peaksoft.house.airbnbb9.dto.request.UpdateBookRequest;
import peaksoft.house.airbnbb9.dto.response.BookResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.Booking;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.Position;
import peaksoft.house.airbnbb9.enums.Role;
import peaksoft.house.airbnbb9.enums.Status;
import peaksoft.house.airbnbb9.exception.BadRequestException;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.repository.BookingRepository;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.service.serviceImpl.BookingServiceImpl;
import org.junit.jupiter.api.Assertions;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @Mock
    private AnnouncementRepository announcementRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Test
    void testFindTakenDates() {
        LocalDate checkIn = LocalDate.of(2023, 9, 8);
        LocalDate checkOut = LocalDate.of(2023, 9, 10);
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking();
        booking1.setCheckIn(LocalDate.of(2023, 9, 7));
        booking1.setCheckOut(LocalDate.of(2023, 9, 9));
        bookings.add(booking1);
        Booking booking2 = new Booking();
        booking2.setCheckIn(LocalDate.of(2023, 9, 11));
        booking2.setCheckOut(LocalDate.of(2023, 9, 13));
        bookings.add(booking2);
        BookingServiceImpl bookingService = new BookingServiceImpl(null, null, null, null);
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            bookingService.findTakenDates(checkIn, checkOut, bookings);
        });
        String expectedMessage = "Intermediate dates of your booking are taken!";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testRequestToBook() throws StripeException {
        User mockUser = new User();
        mockUser.setRole(Role.USER);
        Announcement mockAnnouncement = new Announcement();
        mockAnnouncement.setPosition(Position.ACCEPTED);
        List<Booking> existingBookings = new ArrayList<>();
        Authentication authentication = new UsernamePasswordAuthenticationToken("alister@gmail.com", "user12345678");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userRepository.getUserByEmail(any())).thenReturn(Optional.of(mockUser));
        when(announcementRepository.findById(anyLong())).thenReturn(Optional.of(mockAnnouncement));
        when(bookingRepository.findByAnnouncementId(anyLong())).thenReturn(existingBookings);
        BookRequest bookRequest = new BookRequest();
        bookRequest.setAnnouncementId(9L);
        bookRequest.setCheckIn(LocalDate.of(2023, 9, 15));
        bookRequest.setCheckOut(LocalDate.of(2023, 9, 16));
        bookRequest.setAmount(100.0);
        bookRequest.setToken("tok_1NwS9vLAAx1GCzR7wjwwdrKF");
        BookResponse response = bookingService.requestToBook(bookRequest);
        mockAnnouncement.setStatus(Status.BOOKED);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Booking successful!", response.getMessage());
        verify(bookingRepository).save(any(Booking.class));
    }
    @Test
    public void testUpdateRequestToBook_ValidRequest() throws StripeException {
            User user = new User();
            user.setRole(Role.USER);
            Announcement announcement = new Announcement();
            announcement.setId(9L);
            Booking booking = new Booking();
            booking.setId(6L);
            booking.setUser(user);
            booking.setAnnouncement(announcement);
            booking.setPosition(Position.ACCEPTED);
            UpdateBookRequest request = new UpdateBookRequest();
            request.setBookingId(6L);
            request.setAnnouncementId(9L);
            request.setCheckIn(LocalDate.of(2023, 9, 16));
            request.setCheckOut(LocalDate.of(2023, 9, 17));
            request.setAmount(500.0);
            request.setToken("tok_1Np9yLLAAx1GCzR7kKTO9HgE");
            Authentication authentication = new UsernamePasswordAuthenticationToken("alister@gmail.com", "user12345678");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            when(userRepository.getUserByEmail(any())).thenReturn(Optional.of(user));
            SimpleResponse response = bookingService.updateRequestToBook(request);
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getHttpStatus());
            assertEquals("The dates has been updated! Customer: null", response.getMessage());
            verify(bookingRepository).save(any(Booking.class));
            verify(announcementRepository).save(any(Announcement.class));
        }
    }