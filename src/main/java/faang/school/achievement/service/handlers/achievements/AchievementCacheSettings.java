package faang.school.achievement.service.handlers.achievements;

import faang.school.achievement.model.AchievementCode;

public record AchievementCacheSettings(AchievementCode achievementCode, Long counterThreshold) {
}