package faang.school.achievement.events;

import java.time.LocalDateTime;

public record RecommendationEvent(
        Long recommendationId,
        Long authorId,
        Long receiverId,
        LocalDateTime createdAt) {
}
