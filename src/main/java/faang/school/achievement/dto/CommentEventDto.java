package faang.school.achievement.dto;

import java.time.LocalDateTime;

public record CommentEventDto(
        long postId,
        long authorId,
        long commentId,
        long postAuthorId,
        LocalDateTime createdAt
) {
}
