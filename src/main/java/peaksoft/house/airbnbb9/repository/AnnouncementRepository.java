package peaksoft.house.airbnbb9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.entity.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    boolean existsAnnouncementById(Long announcementId);
}
