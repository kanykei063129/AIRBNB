package peaksoft.house.airbnbb9.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.ZonedDateTime;
import java.util.List;
@Entity
@Table(name = "feedbacks")
@Setter
@Getter
@NoArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "feedback_gen")
    @SequenceGenerator(name = "feedback_gen",sequenceName = "feedback_seq",allocationSize = 1)
    private Long id;
    private String comment;
    @Lob
    private List<String> images;
    private int rating;
    private ZonedDateTime createDate;
    private int likeCount;
    private int disLikeCount;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE})
    private Announcement announcement;
    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE},mappedBy = "feedback")
    private List<Like>likes;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE})
    private User user;

    public Feedback(String comment, List<String> images, int rating, ZonedDateTime createDate, int likeCount, int disLikeCount, Announcement announcement, List<Like> likes) {
        this.comment = comment;
        this.images = images;
        this.rating = rating;
        this.createDate = createDate;
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;
        this.announcement = announcement;
        this.likes = likes;
    }
}
