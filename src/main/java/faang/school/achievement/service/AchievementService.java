package faang.school.achievement.service;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementEventDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.exception.DataNotFoundException;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.filter.achievement.AchievementFilter;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.mapper.UserAchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Builder
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementMapper achievementMapper;
    private final AchievementCache achievementCache;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementMapper userAchievementMapper;
    private final AchievementProgressMapper achievementProgressMapper;
    private final List<AchievementFilter> achievementFilters;
    private final UserContext userContext;



    @Transactional(readOnly = true)
    public List<UserAchievementDto> getAchievementsByUserId() {
        long userId = userContext.getUserId();
        List<UserAchievement> userAchievements = userAchievementRepository.findByUserId(userId);
        return userAchievements.stream()
            .map(userAchievementMapper::toDto)
            .toList();
    }



    @Transactional(readOnly = true)
    public AchievementDto getAchievementById(long id) {
        Achievement achievement = achievementRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Achievement with ID: %d not found", id)));
        return achievementMapper.toDto(achievement);
    }

    @Transactional(readOnly = true)
    public List<AchievementProgressDto> getUserNotAttainedAchievements() {
        long userId = userContext.getUserId();
        List<Achievement> userAttainedAchievements = userAchievementRepository.findByUserId(userId)
                .stream()
                .map(UserAchievement::getAchievement)
                .toList();
        return achievementProgressRepository.findByUserId(userId)
                .stream()
                .filter(achievementProgress -> !userAttainedAchievements.contains(achievementProgress.getAchievement()))
                .map(achievementProgressMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AchievementProgressDto> getAchievementProgressByUserId() {
        long userId = userContext.getUserId();
        List<AchievementProgress> achievementProgresses = achievementProgressRepository.findByUserId(userId);
        return achievementProgresses.stream()
            .map(achievementProgressMapper::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public AchievementDto getAchievementByTitle(String title) {
        Optional<Achievement> cachedAchievement = achievementCache.getAchievementByTitle(title);
        Achievement achievement = cachedAchievement.orElseGet(() -> achievementRepository.findByTitle(title)
            .orElseThrow(() -> new EntityNotFoundException("Achievement with title: %s not found."
                .formatted(title))));

        return achievementMapper.toDto(achievement);
    }

    @Transactional
    public void saveAchievementToUser(long achievementId) {

        long userId = userContext.getUserId();

        Achievement achievement =
                achievementRepository.findById(achievementId).orElseThrow(() -> {
                    log.error("Achievement not found by id: {}", achievementId);
                    return new DataNotFoundException("Achievement not found");
                });

        userAchievementRepository.save(
                UserAchievement.builder()
                        .achievement(achievement)
                        .userId(userId)
                        .build());
    }
}

