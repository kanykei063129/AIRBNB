
package peaksoft.house.airbnbb9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import peaksoft.house.airbnbb9.enums.Role;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private int bookings;
    private int announcements;
    private Role role;
    private List<AnnouncementResponse> announcementResponses;
    private List<BookingResponse> bookingUser;

    public UserResponse(Long id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }
}