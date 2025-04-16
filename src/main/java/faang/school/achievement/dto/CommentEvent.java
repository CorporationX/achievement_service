package faang.school.achievement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CommentEvent(
        Long authorId,
        Long postId,
        Long commentId,
        String title) {
}
