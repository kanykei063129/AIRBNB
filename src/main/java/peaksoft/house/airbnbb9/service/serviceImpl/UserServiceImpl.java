package peaksoft.house.airbnbb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.response.AllAnnouncementResponse;
import peaksoft.house.airbnbb9.exceptoin.NotFoundException;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.repository.UserRepository;
import peaksoft.house.airbnbb9.service.UserService;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
}
