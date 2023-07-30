package peaksoft.house.airbnbb9.dto.responce;

import java.util.List;

public record PaginationAnnouncementResponse(
        List<AnnouncementResponse>announcementResponses,
        int currentPage,
        int pageSize
) {
}
