package peaksoft.house.airbnbb9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.entity.Announcement;

import java.util.List;
import java.util.Optional;
@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    @Query("select new peaksoft.house.airbnbb9.dto.response.AnnouncementResponse(a.id,a.houseType,a.images,a.price,a.region,a.address,a.description,a.status,a.title,a.maxGuests,a.province) from Announcement a")
    List<AnnouncementResponse> getAll();
}
