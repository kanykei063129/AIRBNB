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
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "feedback_gen")
    @SequenceGenerator(name = "feedback_gen",
            sequenceName = "feedback_seq",
            allocationSize = 1,
            initialValue = 6)
    private Long id;
    private String comment;
    @ElementCollection
    private List<String> images;
    private int rating;
    private ZonedDateTime createDate;
    private int likeCount;
    private int disLikeCount;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE})
    private Announcement announcement;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE,
            CascadeType.REMOVE},
            mappedBy = "feedback")
    private List<Like> likes;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private User user;
}
