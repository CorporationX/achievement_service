package faang.school.achievement.dto.event;

import java.time.LocalDateTime;

public record CommentEvent(long postId, long authorId, long commentId, LocalDateTime date) {
}
