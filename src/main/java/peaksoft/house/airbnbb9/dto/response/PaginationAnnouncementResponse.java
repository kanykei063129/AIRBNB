package peaksoft.house.airbnbb9.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class PaginationAnnouncementResponse {
    private List<AnnouncementResponse> announcementResponses;
    private int currentPage;
    private int pageSize;

    public PaginationAnnouncementResponse(List<AnnouncementResponse> announcementResponses, int currentPage, int pageSize) {
        this.announcementResponses = announcementResponses;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public PaginationAnnouncementResponse(List<AnnouncementResponse> announcementResponses) {
        this.announcementResponses = announcementResponses;
    }
}
