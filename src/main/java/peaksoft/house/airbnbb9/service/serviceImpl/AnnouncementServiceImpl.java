package peaksoft.house.airbnbb9.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.responce.AnnouncementResponse;
import peaksoft.house.airbnbb9.repository.AnnouncementRepository;
import peaksoft.house.airbnbb9.service.AnnouncementService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilterBy() {
        return null;
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsSortBy() {
        return null;
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilterByHomeType() {
        return null;
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncementsFilterByPrice() {
        return null;
    }
}
