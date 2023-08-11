package peaksoft.house.airbnbb9.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peaksoft.house.airbnbb9.enums.Position;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "bookings")
@Setter
@Getter
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "booking_gen")
    @SequenceGenerator(name = "booking_gen",
            sequenceName = "booking_seq",
            allocationSize = 1,
            initialValue = 6)
    private Long id;
    private ZonedDateTime checkOut;
    private ZonedDateTime checkIn;
    private BigDecimal pricePerDay;
    private ZonedDateTime date;
    private Position position;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE})
    private User user;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE})
    private Announcement announcement;
    }