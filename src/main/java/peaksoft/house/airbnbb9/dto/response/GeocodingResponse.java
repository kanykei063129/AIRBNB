package peaksoft.house.airbnbb9.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class GeocodingResponse {

    private List<GeocodingResult> results;

}
