package faang.school.achievement.source;

public interface ReloadableAchievementSource extends AchievementSource {
    void reloadCache();
}
