package peaksoft.house.airbnbb9.api.admin;

import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.request.CreatePaymentRequest;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Payment API", description = "API for payment management")
public class PaymentApi {

    private final PaymentService paymentService;

    @PostMapping("/charge")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Charge credit card",
            description = "This method executes payments through the Stripe API using the token, amount, and other payment parameters passed in, as well as random exceptions that may be associated with the payment")
    public SimpleResponse chargeCreditCard(@RequestBody CreatePaymentRequest paymentRequest) throws StripeException {
        return paymentService.chargeCreditCard(paymentRequest);
    }
}