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
       Map<String, Object> chargePrams=new HashMap<>();
       chargePrams.put("amount",(int)(createPaymentRequest.getAmount()*100));
       chargePrams.put("currency","USD");
       chargePrams.put("source","token");
       Charge charge=Charge.create(chargePrams);
       return SimpleResponse
               .builder()
               .httpStatus(HttpStatus.OK)
               .message(charge.getCustomer())
               .build();
    }
    }