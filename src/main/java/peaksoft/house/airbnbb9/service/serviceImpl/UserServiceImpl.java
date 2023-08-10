
package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import peaksoft.house.airbnbb9.dto.response.SimpleResponse;
import peaksoft.house.airbnbb9.dto.response.UserProfileResponse;
import peaksoft.house.airbnbb9.dto.response.UserResponse;
import peaksoft.house.airbnbb9.entity.Like;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.enums.Role;
import peaksoft.house.airbnbb9.exceptoin.BadCredentialException;
import peaksoft.house.airbnbb9.mappers.UserProfileViewMapper;
import peaksoft.house.airbnbb9.repository.LikeRepository;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.repository.template.UserTemplate;
import peaksoft.house.airbnbb9.service.UserService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final UserTemplate userTemplate;
    private final UserProfileViewMapper userProfileViewMapper;

    @Override
    public List<UserResponse> getAllUsers() {
        return userTemplate.getAllUsers();
    }

    @Override
    public SimpleResponse deleteUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id: " + userId + " doesn't exist!"));
        if (user.getRole().equals(Role.ADMIN)) {
            throw new BadCredentialException("you can't remove the admin!!");
        }
        List<Like> userLikes = likeRepository.findByUser(user);
        if (!userLikes.isEmpty()) {
            for (Like like : userLikes) {
                like.setUser(null);
                likeRepository.save(like);
            }
        }
        userRepository.delete(user);
        log.error("User successfully deleted");
        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message(String.format("User with id: %s is successfully deleted", userId))
                .build();
    }

    @Override
    public UserProfileResponse getUserProfile() {
        User user = getAuthenticatedUser();
        return userProfileViewMapper.entityToDto(user);
    }


    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return userRepository.getUserByEmail(login).orElseThrow(() ->
                new BadCredentialException("An unregistered user cannot write comment for this announcement!"));
    }
}