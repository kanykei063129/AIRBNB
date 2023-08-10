package peaksoft.house.airbnbb9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PopularApartmentResponse {
    private List<String> images;
    private String title;
    private String description;
    private String address;
}
