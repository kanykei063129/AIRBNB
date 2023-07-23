package peaksoft.house.airbnbb9.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peaksoft.house.airbnbb9.dto.responce.UserResponse;
import peaksoft.house.airbnbb9.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;
    @GetMapping("getAllUsers")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }
}
