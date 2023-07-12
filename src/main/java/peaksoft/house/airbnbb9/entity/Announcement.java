package peaksoft.house.airbnbb9.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Region;
import peaksoft.house.airbnbb9.enums.Status;
import java.util.List;
@Entity
@Table(name = "announcements")
@Setter
@Getter
@NoArgsConstructor
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "announcement_gen")
    @SequenceGenerator(name = "announcement_gen",sequenceName = "announcement_seq",allocationSize = 1)
    private Long id;
    @Enumerated(EnumType.STRING)
    private HouseType houseType;
    @Lob
    private List<String> images;
    private int price;
    @Enumerated(EnumType.STRING)
    private Region region;
    private String address;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String title;
    private int maxGuests;
    private String province;
    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE},mappedBy = "announcement")
    private List<Feedback>feedbacks;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE})
    private User user;
    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE},mappedBy = "announcement")
    private List<Favorite>favorites;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE})
    private Booking booking;

    public Announcement(HouseType houseType, List<String> images, int price, Region region, String address, String description, Status status, String title, int maxGuests, String province, List<Feedback> feedbacks, User user, List<Favorite> favorites, Booking booking) {
        this.houseType = houseType;
        this.images = images;
        this.price = price;
        this.region = region;
        this.address = address;
        this.description = description;
        this.status = status;
        this.title = title;
        this.maxGuests = maxGuests;
        this.province = province;
        this.feedbacks = feedbacks;
        this.user = user;
        this.favorites = favorites;
        this.booking = booking;
    }
}
