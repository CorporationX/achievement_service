package faang.school.achievement.dto.comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommentEvent(
        Long authorId,
        Long postId,
        Long commentId,
        String content
) {
}
