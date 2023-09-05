package peaksoft.house.airbnbb9.service.serviceImpl;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.request.CreatePaymentRequest;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.service.PaymentService;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Override
    public SimpleResponse chargeCreditCard(CreatePaymentRequest createPaymentRequest) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int) (createPaymentRequest.getAmount() * 100));
        chargeParams.put("currency", "USD");
        chargeParams.put("source", createPaymentRequest.getToken());
        Charge charge = Charge.create(chargeParams);
        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message(charge.getDescription())
                .build();
    }
}