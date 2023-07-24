package peaksoft.house.airbnbb9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("select new peaksoft.house.airbnbb9.dto.response.AnnouncementResponse(a.id,a.houseType,a.images,a.price,a.region,a.address,a.description,a.status,a.title,a.maxGuests,a.province) from Announcement a where a.user.id=:userId")
    Optional<List<AnnouncementResponse>>getUserById(@Param("userId") Long userId);
    @Query("select new peaksoft.house.airbnbb9.dto.response.AnnouncementResponse(a.id, a.houseType, a.images, a.price, a.region, a.address, a.description, a.status, a.title, a.maxGuests, a.province) from Announcement a")
    List<AnnouncementResponse> getAll();
    Optional<User> getUserByEmail(String email);
    Boolean existsByEmail(String email);
}
