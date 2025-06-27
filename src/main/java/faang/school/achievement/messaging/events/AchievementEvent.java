package faang.school.achievement.messaging.events;

public record AchievementEvent(
        Long userId,
        String title
) {
}
