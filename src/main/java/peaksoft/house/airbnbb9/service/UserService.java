package peaksoft.house.airbnbb9.service;

import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.request.UserRequest;
import peaksoft.house.airbnbb9.entity.User;

@Service
public interface UserService {
    public User saveUser(String fullName, String email);
}
