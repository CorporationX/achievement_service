package faang.school.achievement.service.interfaces;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.model.AchievementProgress;

import java.util.List;

public interface AchievementService {

    AchievementDto get(String title);

    List<AchievementDto> getAll();

    boolean hasAchievement(long userId, long achievementId);

    void createProgressIfNecessary(long userId, long achievementId);

    AchievementProgress getProgress(long userId, long achievementId);

    void updateProgress(AchievementProgress achievementProgress);

    void giveAchievement(long userId, long achievementId);
}
