package peaksoft.house.airbnbb9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.entity.Announcement;
import peaksoft.house.airbnbb9.entity.Feedback;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement,Long> {
    @Query("select new peaksoft.house.airbnbb9.dto.response.AnnouncementResponse(a.id, a.houseType, a.images, a.price, a.region, a.address, a.description, a.status, a.title, a.maxGuests, a.province) from Announcement a")
    List<AnnouncementResponse> getAll();

    @Query("SELECT u.fullName, u.email, f FROM Announcement a JOIN a.feedbacks f JOIN a.user u WHERE a.id = ?1")
    List<Feedback> getAllAnnouncementFeedback(@Param("id") Long id);
}
