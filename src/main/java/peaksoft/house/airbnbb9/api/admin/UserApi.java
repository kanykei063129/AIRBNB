package peaksoft.house.airbnbb9.api.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.dto.response.UserResponse;
import peaksoft.house.airbnbb9.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "UserApi", description = "Controller for Users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserApi {

    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all users", description = "Available only to registered users")
    @GetMapping()
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Delete user", description = "Delete user by id ")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{userId}")
    public SimpleResponse deleteUser(@PathVariable Long userId) {
        return userService.deleteUserById(userId);
    }

    @Operation(summary = "users", description = " get by id and only for bookings and announcements")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get/{userId}")
    public UserResponse getUserById(@PathVariable Long userId, @RequestParam String value) {
        return userService.getByIdUser(userId, value);
    }
}