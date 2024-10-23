package faang.school.achievement.service.achievement;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.filter.AchievementFilter;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.AchievementPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.CacheService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {

    private static final String ACHIEVEMENTS_CACHE_NAME_BY_TITLE = "achievements-by-title";
    private static final String ACHIEVEMENTS_CACHE_NAME_BY_ID = "achievements-by-id";
    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository achievementUserRepository;
    private final CacheService<Achievement> cacheService;
    private final AchievementPublisher achievementPublisher;
    private final List<AchievementFilter> achievementFilters;
    private final AchievementMapper achievementMapper;

    @PostConstruct
    public void initAchievements() {
        List<Achievement> achievements = achievementRepository.findAll();
        achievements.forEach(achievement -> {
            cacheService.put(ACHIEVEMENTS_CACHE_NAME_BY_TITLE, achievement.getTitle(), achievement);
            cacheService.put(ACHIEVEMENTS_CACHE_NAME_BY_ID, achievement.getId().toString(), achievement);
        });
    }

    @Override
    public boolean hasAchievement(long userId, long achievementId) {
        return achievementUserRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Override
    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Achievement %d progress not found".formatted(achievementId)));
    }

    @Override
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Override
    public void giveAchievement(long userId, Achievement achievement) {
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();
        achievementUserRepository.save(userAchievement);

        AchievementEvent event = new AchievementEvent(LocalDateTime.now(), userId, achievement.getId());
        achievementPublisher.publish(event);
    }

    @Override
    public List<AchievementDto> getAchievements(AchievementFilterDto filterDto) {
        List<Achievement> achievements = cacheService.getValuesFromMap(ACHIEVEMENTS_CACHE_NAME_BY_TITLE, Achievement.class);
        return achievementFilters.stream()
                .filter(filter -> filter.isAccepted(filterDto))
                .reduce(achievements.stream(),
                        (stream, filter) -> filter.apply(stream, filterDto),
                        Stream::concat)
                .map(achievementMapper::toDto)
                .toList();
    }

    @Override
    public List<AchievementDto> getAchievementsBy(long userId) {
        List<Achievement> userAchievements = achievementUserRepository.findByUserIdAchievements(userId);
        return achievementMapper.toDto(userAchievements);
    }

    @Override
    public AchievementDto getAchievement(long achievementId) {
        Achievement achievement = cacheService.getFromMap(
                ACHIEVEMENTS_CACHE_NAME_BY_ID,
                Long.toString(achievementId),
                Achievement.class
        );
        return Optional.ofNullable(achievement)
                .map(achievementMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Achievement %d not found".formatted(achievementId)));
    }

    @Override
    public List<AchievementDto> getNotReceivedAchievements(long userId) {
        List<Achievement> notReceivedAchievements = achievementRepository.findUnobtainedAchievementsWithProgressByUserId(userId);
        return achievementMapper.toDto(notReceivedAchievements);
    }
}
