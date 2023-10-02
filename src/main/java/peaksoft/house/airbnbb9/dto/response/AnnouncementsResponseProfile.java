package peaksoft.house.airbnbb9.dto.response;

import lombok.*;
import peaksoft.house.airbnbb9.enums.HouseType;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementsResponseProfile {
    private Long id;
    private String title;
    private List<String> images;
    private HouseType houseType;
    private int maxGuests;
    private String address;
    private String description;
    private String fullName;
    private String email;
    private String image;
    private String bookedByFullName;
    private String bookedByEmail;
    private Integer priceDay;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String favoriteByFullName;
    private String favoriteByEmail;
    private String comments;
    private LocalDate createDateFeedback;
    private Integer feedbackRating;
    private Integer likeCount;
    private Integer disLikeCount;
    private String feedbacksUserName;
    private String feedbacksImages;
}
