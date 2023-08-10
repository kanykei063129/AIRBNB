package peaksoft.house.airbnbb9.dto.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalSearchResponse {
    List<AnnouncementResponse> announcementResponses;
}
