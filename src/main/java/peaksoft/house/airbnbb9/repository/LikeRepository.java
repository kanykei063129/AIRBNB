package peaksoft.house.airbnbb9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.entity.Like;
import peaksoft.house.airbnbb9.entity.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {

    List<Like> findByUser(User user);

    List<Like> getLikeByFeedbackId(Long feedbackId);

    @Query("SELECT COUNT(l.id) FROM Like l JOIN l.feedback f WHERE f.id = ?1 AND l.isLiked = ?2")
    int getCountLikeOrDislikeByFeedbackId(Long feedbackId, boolean isLike);

    Optional<Like> getLikeByUserId(Long userId);

}