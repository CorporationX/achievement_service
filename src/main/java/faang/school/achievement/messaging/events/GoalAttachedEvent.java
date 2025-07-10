package faang.school.achievement.messaging.events;

import java.time.LocalDateTime;

public record GoalAttachedEvent(Long userId, Long goalId, String goalTitle, LocalDateTime time) {
}
