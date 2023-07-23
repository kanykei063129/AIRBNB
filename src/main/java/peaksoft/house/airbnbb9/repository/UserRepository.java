package peaksoft.house.airbnbb9.repository;

import org.springframework.stereotype.Repository;
import peaksoft.house.airbnbb9.entity.User;

import java.util.Optional;
@Repository
public interface UserRepository {
    Optional<User> getUserByEmail(String email);
}
