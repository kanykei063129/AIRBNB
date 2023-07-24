package peaksoft.house.airbnbb9.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
    String email,
    String token){
}
