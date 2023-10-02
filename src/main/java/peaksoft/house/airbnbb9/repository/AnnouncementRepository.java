package peaksoft.house.airbnbb9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.entity.Announcement;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    boolean existsAnnouncementById(Long announcementId);

    @Query("select a.images from Announcement a where a.id =:id")
    List<String> getAnnouncementImages(@Param("id") Long announcementId);

    @Query("select a from Announcement a where a.user.id=:id")
    List<Announcement> getAnnouncementByUserId(@Param("id") Long userId);
}
