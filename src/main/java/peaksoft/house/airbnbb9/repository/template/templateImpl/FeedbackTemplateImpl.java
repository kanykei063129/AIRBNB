package peaksoft.house.airbnbb9.repository.template.templateImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import peaksoft.house.airbnbb9.dto.response.FeedbackResponse;
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
                      f.rating                   AS rating,
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
}

