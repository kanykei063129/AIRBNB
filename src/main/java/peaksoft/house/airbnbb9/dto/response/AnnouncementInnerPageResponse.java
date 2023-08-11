package peaksoft.house.airbnbb9.dto.response;

import lombok.Getter;
import lombok.Setter;
import peaksoft.house.airbnbb9.enums.HouseType;
import peaksoft.house.airbnbb9.enums.Region;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AnnouncementInnerPageResponse {

    private Long id;
    private List<String> images;
    private HouseType houseType;
    private Integer maxGuests;
    private BigDecimal price;
    private String description;
    private Region region;
    private String province;
    private String address;
    private Long userID;
    private String userImage;
    private String userFullName;
    private String userEmail;
    private List<BookedResponse> announcementBookings;
    private List<FeedbackResponse> feedbackResponses;
}
