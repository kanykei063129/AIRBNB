package peaksoft.house.airbnbb9.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record PaginationAnnouncementResponse(
        List<AnnouncementResponse>announcementResponses,
        int currentPage,
        int pageSize
) {
    public PaginationAnnouncementResponse(List<AnnouncementResponse> announcementResponses) {
        this(announcementResponses, 0, 0);
    }
}
