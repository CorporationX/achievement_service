package faang.school.achievement.redis;

import org.springframework.stereotype.Component;

@Component
public class AchievementKeyBuilder {
    private static final String PREFIX = "achievement";

    public String achievementKey(String title) {
        return String.format("%s:%s", PREFIX, title);
    }

    public String userProgress(String title, long userId) {
        return String.format("%s:%s:user:%d:progress", PREFIX, title, userId);
    }

    public String assignAchievement(long userId) {
        return String.format("%s:user:%d:completed", PREFIX, userId);
    }
}