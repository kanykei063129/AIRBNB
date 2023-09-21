package peaksoft.house.airbnbb9.dto.response;

import lombok.*;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
public class FilterResponse {
    private List<ProfileAnnouncementResponse> responses;

    public FilterResponse(List<ProfileAnnouncementResponse> responses) {
        this.responses = responses;
    }
}
