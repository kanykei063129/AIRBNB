package peaksoft.house.airbnbb9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
}
