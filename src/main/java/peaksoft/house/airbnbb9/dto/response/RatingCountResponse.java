package peaksoft.house.airbnbb9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RatingCountResponse {
    private double one;
    private double two;
    private double three;
    private double four;
    private double five;
    private double averageRating;
}
