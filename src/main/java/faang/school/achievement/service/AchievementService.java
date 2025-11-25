package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressResponseDto;
import faang.school.achievement.dto.AchievementResponseDto;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievementStatus;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface AchievementService {

    List<AchievementResponseDto> getAllAchievementsUser(@Positive long userId);

    List<AchievementResponseDto> getAllAchievements(AchievementFilterDto filterDto);

    AchievementResponseDto getAchievementsById(@Positive long achievementId);

    List<AchievementProgressResponseDto> getUserPendingAchievementsWithProgress(@Positive long userId,
                                                                                UserAchievementStatus status);

    boolean hasAchievement(@Positive long userId, @Positive long achievementId);

    void createProgressIfNecessary(@Positive long userId, @Positive long achievementId);

    Optional<AchievementProgress> getProgress(@Positive long userId, @Positive long achievementId);

    void giveAchievement(@Positive long userId, @Positive long achievementId);

    void saveProgress(AchievementProgress achievementProgress);
}
