package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;

import java.util.List;

public interface AchievementRequestService {

    List<AchievementDto> getAllAchievements(AchievementFilterDto filter);

    List<UserAchievementDto> getUserAchievements(Long userId);

    AchievementDto getAchievementById(Long id);

    List<AchievementProgressDto> getUserUnearnedAchievements(Long userId);
}
