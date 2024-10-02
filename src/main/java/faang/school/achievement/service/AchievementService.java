package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.exception.NotFoundException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.mapper.UserAchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.redis.AchievementPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.util.filter.AchievementFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementMapper achievementMapper;
    private final UserAchievementMapper userAchievementMapper;
    private final AchievementProgressMapper achievementProgressMapper;
    private final List<AchievementFilter> filters;
    private final AchievementPublisher achievementPublisher;

    public List<AchievementDto> getAllAchievement(AchievementFilterDto filterDto) {
        List<Achievement> achievements = achievementRepository.findAll();

        if(filterDto != null) {
            Stream<Achievement> achievementStream = achievements.stream();
            achievements = filters.stream()
                    .filter(filter -> filter.isApplicable(filterDto))
                    .reduce(achievementStream,
                            (achievementStream1, filter) -> filter.apply(achievementStream1, filterDto),
                            Stream::concat)
                    .toList();
        }

        log.info("Received all achievements");
        return achievementMapper.toDtoList(achievements);
    }

    public List<UserAchievementDto> getAchievementsByUserId(Long userId) {
        List<UserAchievement> userAchievements = userAchievementRepository.findByUserId(userId);
        if (userAchievements == null) {
            throw new NotFoundException("No achievements found for user with ID = " + userId);
        }
        log.info("Received achievements for user with ID = {}", userId);
        return userAchievementMapper.toDtoList(userAchievements);
    }

    public AchievementDto getAchievement(Long id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Achievement with ID = " + id + " not found"));
        log.info("Found achievement with ID = {}", id);
        return achievementMapper.toDto(achievement);
    }

    public List<AchievementProgressDto> getAllAchievementsProgressForUser(Long userId) {
        List<AchievementProgress> achievementProgresses = achievementProgressRepository.findByUserId(userId);
        if (achievementProgresses == null) {
            throw new NotFoundException("No achievements in progress found for user with ID = " + userId);
        }
        log.info("Found achievements in progress for user with ID = {}", userId);
        return achievementProgressMapper.toDtoList(achievementProgresses);
    }

    @Transactional(readOnly = true)
    public boolean userHasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Transactional(readOnly = true)
    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Achievement progress with user id=%d and achievement id=%d not found!",
                        userId, achievementId
                )));
    }

    @Transactional
    public void saveProgress(AchievementProgress progress) {
        achievementProgressRepository.save(progress);
    }

    @Transactional
    public void giveAchievement(long userId, Achievement achievement) {
        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievement)
                .userId(userId)
                .build();

        userAchievement = userAchievementRepository.save(userAchievement);

        achievementPublisher.publishMessage(userAchievementMapper.toEvent(userAchievement));
    }

    @Transactional
    public AchievementProgress incrementCurrentPointsForUser(AchievementProgress progress) {
        Optional<AchievementProgress> achievementProgress = achievementProgressRepository.incrementCurrentPointsForUser(progress.getId());
        return achievementProgress.orElseThrow(() -> new NotFoundException("Progress with ID = " + progress.getId() + " not found!"));
    }
}
