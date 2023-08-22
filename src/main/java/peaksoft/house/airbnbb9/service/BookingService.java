package peaksoft.house.airbnbb9.service;

import peaksoft.house.airbnbb9.dto.request.BookRequest;
import peaksoft.house.airbnbb9.dto.request.UpdateBookRequest;

import java.util.Map;

public interface BookingService {
    Map<String, String> requestToBook(BookRequest request);

    Map<String, String> updateRequestToBook(UpdateBookRequest request);
}
