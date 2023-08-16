package peaksoft.house.airbnbb9.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import peaksoft.house.airbnbb9.dto.response.BookedResponse;
import peaksoft.house.airbnbb9.entity.Booking;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class BookingViewMapper {

    public BookedResponse viewBooked(Booking request) {
        if (request == null) {
            return null;
        }

        log.info("Creating BookedResponse for booking ID: " + request.getId());

        BookedResponse response = new BookedResponse();
        response.setBookingId(request.getId());
        response.setPrice(request.getPricePerDay());
        response.setCheckIn(LocalDate.from(request.getCheckIn()));
        response.setCheckOut(LocalDate.from(request.getCheckOut()));
        response.setStatus(request.getPosition().name());
        response.setUserName(request.getUser().getFullName());
        response.setUserEmail(request.getUser().getEmail());
        response.setUserImage(request.getUser().getImage());

        log.info("Created BookedResponse successfully for booking ID: " + request.getId());
        return response;
    }
    public List<BookedResponse> viewBooked(List<Booking> requests) {
        List<BookedResponse> responses = new ArrayList<>();

        log.info("Creating BookedResponses for a list of bookings");

        for (Booking a : requests) {
            responses.add(viewBooked(a));
        }
        log.info("Created BookedResponses successfully for the list of bookings");
        return responses;
    }
}
