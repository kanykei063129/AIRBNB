package peaksoft.house.airbnbb9.dto;

import lombok.Builder;

@Builder
public record SignInRequest(
        String email,
        String password
) {
}
