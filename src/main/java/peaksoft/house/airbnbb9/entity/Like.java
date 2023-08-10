package peaksoft.house.airbnbb9.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "likes")
@Setter
@Getter
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "like_gen")
    @SequenceGenerator(name = "like_gen",
            sequenceName = "like_seq",
            allocationSize = 1,
            initialValue = 6)
    private Long id;
    private Boolean isLiked;

    @OneToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE})
    private User user;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.MERGE})
    private Feedback feedback;
}
