package peaksoft.house.airbnbb9.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class UserProfileResponse {
    private String image;
    private String name;
    private String contact;
    private List<String> messageFromAdmin;
    private List<UserBookingsResponse> bookings;
    private List<UserAnnouncementResponse> announcements;
}
