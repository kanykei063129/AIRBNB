package peaksoft.house.airbnbb9.dto.response;

import lombok.*;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
public class FilterResponse {
    private List<AnnouncementResponse> responses;

    public FilterResponse(List<AnnouncementResponse> responses) {
        this.responses = responses;
    }
}
