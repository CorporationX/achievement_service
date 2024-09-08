package faang.school.achievement.service.achievement;

import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.achievement.AchievementDto;
import faang.school.achievement.dto.achievement.AchievementFilterDto;
import faang.school.achievement.dto.achievement.AchievementProgressDto;
import faang.school.achievement.dto.achievement.UserAchievementDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.achievement.filter.AchievementFilter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Service
public class AchievementService {

    private final UserContext userContext;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementMapper achievementMapper;
    private final List<AchievementFilter> achievementFilters;

    public List<AchievementDto> getAchievementsByFilter(AchievementFilterDto filterDto) {
        Stream<Achievement> achievementStream = StreamSupport.stream(
                achievementRepository.findAll().spliterator(),
                false
        );
        achievementStream = achievementFilters.stream()
                .filter(filter -> filter.isApplicable(filterDto))
                .reduce(
                        achievementStream,
                        (stream, filter) -> filter.apply(stream, filterDto),
                        (s1, s2) -> s1
                );
        return achievementStream.map(achievementMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserAchievementDto> getUserAchievementsByUserId() {
        long userId = userContext.getUserId();
        return userAchievementRepository.findByUserId(userId).stream()
                .map(achievementMapper::toDto)
                .collect(Collectors.toList());
    }

    public AchievementDto getAchievementById(long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new IllegalArgumentException("Achievement with id " + achievementId + " doesn't exist"));
        return achievementMapper.toDto(achievement);
    }

    public List<AchievementProgressDto> getInProgressUserAchievementsByUserId() {
        long userId = userContext.getUserId();
        return achievementProgressRepository.findByUserId(userId).stream()
                .map(achievementMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean hasAchievement(Long userId, Long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void createProgressIfNecessary(Long userId, Long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Transactional
    public void giveAchievement(Long userId, Achievement achievement) {
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();
        userAchievementRepository.save(userAchievement);
    }

    @Transactional
    public AchievementProgress getProgress(Long userId, Long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Progress not found"));
    }

    @Transactional
    public void updateProgress(AchievementProgress progress) {
        achievementProgressRepository.save(progress);
    }
}
