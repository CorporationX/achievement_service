package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class EventHandler<T> {
    protected final AchievementService achievementService;
    protected final AchievementCache achievementCache;

    public abstract void handle(T event);
}
