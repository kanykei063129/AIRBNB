package peaksoft.house.airbnbb9.entity;
import jakarta.persistence.*;
import lombok.*;
import peaksoft.house.airbnbb9.enums.Role;
import java.time.ZonedDateTime;
@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", allocationSize = 1)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private ZonedDateTime dateOfBirth;
    private String phoneNumber;
    private Role role;
}
