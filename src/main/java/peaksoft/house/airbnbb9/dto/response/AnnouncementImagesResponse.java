package peaksoft.house.airbnbb9.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AnnouncementImagesResponse {
    private List<String> images;
}
