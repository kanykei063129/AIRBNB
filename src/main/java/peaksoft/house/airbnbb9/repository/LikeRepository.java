package peaksoft.house.airbnbb9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
}
