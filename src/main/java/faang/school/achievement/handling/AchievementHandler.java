package faang.school.achievement.handling;

public interface AchievementHandler<T> {
    void handleAchievement(T event);
}