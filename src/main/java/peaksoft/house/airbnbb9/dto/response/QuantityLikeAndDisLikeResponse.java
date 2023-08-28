package peaksoft.house.airbnbb9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class QuantityLikeAndDisLikeResponse {

    private int likeCount;
    private int disLikeCount;
}
