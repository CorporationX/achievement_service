package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressResponseDto;
import faang.school.achievement.dto.AchievementResponseDto;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AchievementService {

    List<AchievementResponseDto> getAllAchievementsUser(@Positive long userId);

    List<AchievementResponseDto> getAllAchievements(AchievementFilterDto filterDto);

    AchievementResponseDto getAchievementsUserById(@Positive long achievementsId);

    List<AchievementProgressResponseDto> getUserPendingAchievementsWithProgress(@Positive long userId);
}
