package peaksoft.house.airbnbb9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.house.airbnbb9.dto.response.AnnouncementResponse;
import peaksoft.house.airbnbb9.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("select new peaksoft.house.airbnbb9.dto.response.AnnouncementResponse(a.id,a.houseType,a.images,a.price,a.region,a.address,a.description,a.status,a.title,a.maxGuests,a.province) from Announcement a where a.user.id= ?1")
    Optional<List<AnnouncementResponse>>getUserById(Long userId);
    Optional<User> getUserByEmail(String email);
    Boolean existsByEmail(String email);
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.entity.User;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> getUserByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findUserByEmail(String email);
}
