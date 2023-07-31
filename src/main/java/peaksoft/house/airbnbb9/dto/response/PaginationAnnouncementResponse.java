package peaksoft.house.airbnbb9.dto.response;

import java.util.List;

public record PaginationAnnouncementResponse(
        List<AnnouncementResponse>announcementResponses,
        int currentPage,
        int pageSize
) {
}
