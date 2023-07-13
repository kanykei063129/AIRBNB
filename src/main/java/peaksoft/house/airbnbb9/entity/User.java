package peaksoft.house.airbnbb9.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peaksoft.house.airbnbb9.enums.Role;

import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_gen")
    @SequenceGenerator(name = "user_gen",sequenceName = "user_seq",allocationSize = 1)
    private Long id;
    private String fullName;
    private String email;
    private String image;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE},
            mappedBy = "user")
    private List<Feedback> feedbacks;

    @OneToMany(cascade = {
            CascadeType.REMOVE,
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE},
            mappedBy = "user")
    private List<Announcement>announcements;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE,
            CascadeType.REMOVE},
            mappedBy = "user")
    private List<Favorite>favorites;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE,
            CascadeType.REMOVE},
            mappedBy = "user")
    private List<Booking>bookings;
}
