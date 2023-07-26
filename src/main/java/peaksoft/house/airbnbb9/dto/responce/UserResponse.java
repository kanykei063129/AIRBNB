
package peaksoft.house.airbnbb9.dto.responce;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;
import peaksoft.house.airbnbb9.enums.Role;

@Data
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private int bookings;
    private int announcements;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String token;

    public UserResponse(Long id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }
}
