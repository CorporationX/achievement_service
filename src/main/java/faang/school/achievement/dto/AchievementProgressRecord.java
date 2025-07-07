package faang.school.achievement.dto;

import faang.school.achievement.model.Achievement;

public record AchievementProgressRecord(Achievement achievement, long userId, long currentPoints) {
}
