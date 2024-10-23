package faang.school.achievement.handler;

import faang.school.achievement.dto.ProjectEvent;
import faang.school.achievement.model.Achievement;

public interface AchievementHandler<T> {
    void handleAchievement(T event);
    boolean hasAchievement(ProjectEvent event, Achievement businessman);
}
