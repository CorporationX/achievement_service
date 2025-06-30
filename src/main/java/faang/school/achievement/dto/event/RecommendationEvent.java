package faang.school.achievement.dto.event;

public record RecommendationEvent(
        long authorId,
        long receiverId,
        String text
) {
}
