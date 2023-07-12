package peaksoft.house.airbnbb9.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Setter
@Getter
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "booking_gen")
    @SequenceGenerator(name = "booking_gen",sequenceName = "booking_seq",allocationSize = 1)
    private Long id;
    private ZonedDateTime checkOut;
    private ZonedDateTime checkIn;
    private ZonedDateTime date;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE})
    private User user;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE},
            mappedBy = "booking")
    private List<Announcement> announcements;
    }