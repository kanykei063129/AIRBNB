package peaksoft.house.airbnbb9.dto.request;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class
UserRequest {
    private String fullName;
    private String email;
    private String password;
}
