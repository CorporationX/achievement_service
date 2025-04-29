package faang.school.achievement.service.achievement;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.model.Achievement;

import java.util.List;

public interface AchievementService {

    Achievement getAchievement(long achievementId);

    List<AchievementDto> getAchievements();
}
