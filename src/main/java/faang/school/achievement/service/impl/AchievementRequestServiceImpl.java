package faang.school.achievement.service.impl;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.AchievementRequestService;

import faang.school.achievement.validator.AchievementPageableValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementRequestServiceImpl implements AchievementRequestService {

    private final AchievementCache achievementCache;
    private final AchievementMapper achievementMapper;
    private final AchievementProgressMapper achievementProgressMapper;
    private final AchievementPageableValidator pageableValidator;


    @Override
    public List<AchievementDto> getAllAchievements(AchievementFilterDto filter) {
        pageableValidator.validateAndSetDefaults(filter);

        List<Achievement> achievements = achievementCache.findFilteredAchievements(
                filter.getTitle(),
                filter.getDescription(),
                filter.getRarity(),
                filter.getPageable()
        );
        return achievementMapper.toDtoList(achievements);
    }

    @Override
    public List<UserAchievementDto> getUserAchievements(Long userId) {
        List<UserAchievement> achievements = achievementCache.getUserAchievements(userId);
        return achievementMapper.toUserAchievementDtoList(achievements);
    }

    @Override
    public AchievementDto getAchievementById(Long id) {
        Achievement achievement = achievementCache.getAchievementById(id);
        return achievementMapper.toDto(achievement);
    }

    @Override
    public List<AchievementProgressDto> getUserUnearnedAchievements(Long userId) {
        List<AchievementProgress> achievements = achievementCache.getUserUnearnedAchievements(userId);
        return achievementProgressMapper.toDtoList(achievements);
    }
}
