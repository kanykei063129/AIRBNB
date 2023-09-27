package peaksoft.house.airbnbb9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.entity.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByAnnouncementId(Long announcementId);

    @Query("SELECT b from Booking b where b.user.id = :id")
    Booking getBookingByUserId(@Param("id")Long userid);
}
