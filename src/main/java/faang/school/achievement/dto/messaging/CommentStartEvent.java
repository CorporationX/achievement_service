package faang.school.achievement.dto.messaging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommentStartEvent(Long commentId, String comment, Long userId, Long postId, LocalDateTime createdAt) {
}
