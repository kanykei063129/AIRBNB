package peaksoft.house.airbnbb9.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Position;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "announcements")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "announcement_gen")
    @SequenceGenerator(name = "announcement_gen",
            sequenceName = "announcement_seq",
            allocationSize = 1,
            initialValue = 6)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "house_type")
    private HouseType houseType;
    @ElementCollection
    private List<String> images;
    @Column(name = "price")
    private int price;
    @Enumerated(EnumType.STRING)
    @Column(name = "region")
    private Region region;
    @Column(name = "address")
    private String address;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    @Column(name = "title")
    private String title;
    @Column(name = "max_guests")
    private int maxGuests;
    @Column(name = "province")
    private String province;
    @Column(name = "create_date")
    private LocalDate createDate;
    private String messageFromAdmin;
    @Enumerated(EnumType.STRING)
    private Position position;
    @ElementCollection
    private List<LocalDate> blockedDates;
    @ElementCollection
    private List<LocalDate> blockedDatesByUser;

    @OneToMany(mappedBy = "announcement", cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE,
            CascadeType.REMOVE})
    private List<Feedback> feedbacks;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE})
    private User user;

    @OneToMany(mappedBy = "announcement", cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE,
            CascadeType.REMOVE})
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "announcement", cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE,
            CascadeType.REMOVE})
    private List<Booking> bookings;

    public void addBlockedDateByUser(LocalDate date) {
        this.blockedDatesByUser.add(date);
    }

    public void removeBlockedDateByUser(LocalDate date) {
        this.blockedDatesByUser.remove(date);
    }
    public void releaseTakenDates(LocalDate checkin, LocalDate checkout) {
        while (checkin.isBefore(checkout)) {
            this.blockedDates.remove(checkin);
            checkin = checkin.plusDays(1L);
        }
        this.blockedDates.remove(checkout);
    }
}