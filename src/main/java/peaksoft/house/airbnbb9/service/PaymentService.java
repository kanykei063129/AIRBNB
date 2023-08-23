package peaksoft.house.airbnbb9.service;

import com.stripe.exception.StripeException;
import peaksoft.house.airbnbb9.dto.request.CreatePaymentRequest;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;

public interface PaymentService {

    SimpleResponse chargeCreditCard(CreatePaymentRequest createPaymentRequest) throws StripeException;
}
