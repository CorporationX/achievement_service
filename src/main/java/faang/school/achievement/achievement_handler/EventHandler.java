package faang.school.achievement.achievement_handler;

public interface EventHandler<T> {
    void proceedAchievement(long userId);
}
