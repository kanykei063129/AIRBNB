package peaksoft.house.airbnbb9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peaksoft.house.airbnbb9.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
