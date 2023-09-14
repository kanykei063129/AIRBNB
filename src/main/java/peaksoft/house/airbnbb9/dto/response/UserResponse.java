
package peaksoft.house.airbnbb9.dto.response;

import lombok.*;
import peaksoft.house.airbnbb9.enums.Role;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private int bookings;
    private int announcements;
    private Role role;
    private List<AnnouncementResponseUser> announcementResponses;
    private List<BookingResponse> bookingUser;

    public UserResponse(Long id, String fullName, String email, int bookings, int announcements, Role role, List<AnnouncementResponseUser> announcementResponses, List<BookingResponse> bookingUser) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.bookings = bookings;
        this.announcements = announcements;
        this.role = role;
        this.announcementResponses = announcementResponses;
        this.bookingUser = bookingUser;
    }

    public UserResponse(Long id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    public UserResponse(List<AnnouncementResponseUser> announcementResponses) {
        this.announcementResponses = announcementResponses;
    }
}