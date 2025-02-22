package faang.school.achievement.handler;

public interface AchievementHandler<T> {

    void applyAchievement(T event);

}