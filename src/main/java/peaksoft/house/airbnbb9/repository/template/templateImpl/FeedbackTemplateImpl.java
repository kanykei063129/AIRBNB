package peaksoft.house.airbnbb9.repository.template.templateImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.response.FeedbackResponse;
import peaksoft.house.airbnbb9.dto.response.RatingCountResponse;
import peaksoft.house.airbnbb9.exception.NotFoundException;
import peaksoft.house.airbnbb9.repository.template.FeedbackTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FeedbackTemplateImpl implements FeedbackTemplate {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<FeedbackResponse> getAllFeedback(Long announcementId) {
        String sql = """
                SELECT f.id,
                       u.full_name                AS feedbackUserFullName,
                       u.image                    AS feedbackUserImage,
                       f.rating                    AS rating ,
                       f.comment                  AS comment,
                       f.create_date              AS createdAt,
                       f.like_count               AS likeCount,
                       f.dis_like_count           AS disLikeCount,
                       string_agg(fi.images, ',') AS images
                FROM feedbacks f
                         LEFT JOIN
                     feedback_images fi ON fi.feedback_id = f.id
                         LEFT JOIN
                     users u ON u.id = f.user_id
                WHERE f.announcement_id = ?
                GROUP BY f.id,
                         u.full_name,
                         u.image,
                         f.rating,
                         f.comment,
                         f.create_date,
                         f.like_count,
                         f.dis_like_count
                 """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> FeedbackResponse
                .builder()
                .id(rs.getLong("id"))
                .feedbackUserImage(rs.getString("feedbackUserImage"))
                .feedbackUserFullName(rs.getString("feedbackUserFullName"))
                .rating(rs.getInt("rating"))
                .comment(rs.getString("comment"))
                .images(Arrays.asList(rs.getString("images").split(",")))
                .createdAt(rs.getDate("createdAt").toLocalDate())
                .likeCount(rs.getInt("likeCount"))
                .disLikeCount(rs.getInt("disLikeCount"))
                .build(), announcementId);
    }

    @Override
    public RatingCountResponse countRating(Long announcementId) {
        String sql = """
                  SELECT
                    (SELECT count(*) from feedbacks f where f.announcement_id = a.id and rating = 5) as five,
                    (select count(*) from feedbacks f where f.announcement_id = a.id and rating = 4) as four,
                    (select count(*) from feedbacks f where f.announcement_id = a.id and rating = 3) as three,
                    (select count(*) from feedbacks f where f.announcement_id = a.id and rating = 2) as two,
                    (select count(*) from feedbacks f where f.announcement_id = a.id and rating = 1) as one,
                    (select avg(f.rating) from feedbacks f where f.announcement_id = a.id) as rating,
                    (select count(*) from feedbacks f where f.announcement_id  = a.id) as total_feedbacks
                from announcements a where id = ?;
                """;
        return jdbcTemplate.query(sql, (rs, i) -> {
                    RatingCountResponse rw = new RatingCountResponse();
                    int totalFeedbacks = rs.getInt("total_feedbacks");
                    rw.setFive(Math.round((rs.getDouble("five") * 100) / totalFeedbacks));
                    rw.setFour(Math.round((rs.getDouble("four") * 100) / totalFeedbacks));
                    rw.setThree(Math.round((rs.getDouble("three") * 100) / totalFeedbacks));
                    rw.setTwo(Math.round((rs.getDouble("two") * 100) / totalFeedbacks));
                    rw.setOne(Math.round((rs.getDouble("one") * 100) / totalFeedbacks));
                    rw.setAverageRating(rs.getDouble("rating"));
                    return rw;
                }, announcementId).stream().findAny()
                .orElseThrow(() -> new NotFoundException("The index not found!"));
    }
}