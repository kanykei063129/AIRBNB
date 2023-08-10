package peaksoft.house.airbnbb9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PopularHouseResponse {
    private String title;
    private String image;
    private String address;
    private int price;
    private double rating;
}
