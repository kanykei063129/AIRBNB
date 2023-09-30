package peaksoft.house.airbnbb9.service;

import com.stripe.exception.StripeException;
import peaksoft.house.airbnbb9.dto.request.BookRequest;
import peaksoft.house.airbnbb9.dto.request.UpdateBookRequest;
import peaksoft.house.airbnbb9.dto.response.BookResponse;
import peaksoft.house.airbnbb9.dto.response.BookingResponse;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;

import java.util.Map;

public interface BookingService {
    BookResponse requestToBook(BookRequest request) throws StripeException;

    SimpleResponse updateRequestToBook(UpdateBookRequest request) throws StripeException;
}
