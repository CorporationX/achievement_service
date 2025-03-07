package faang.school.achievement.service;

import faang.school.achievement.dto.achievement.AchievementFilterDto;
import faang.school.achievement.dto.achievement.AchievementProgressReadDto;
import faang.school.achievement.dto.achievement.AchievementReadDto;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.filter.achievement.AchievementFilter;
import faang.school.achievement.mapper.achievement.AchievementMapper;
import faang.school.achievement.mapper.achievement.AchievementProgressMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementMapper achievementMapper;
    private final AchievementProgressMapper achievementProgressMapper;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final List<AchievementFilter> achievementFilters;

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Прогресс достижения ID %s у пользователя ID %s не найден"
                        .formatted(achievementId, userId)));
    }

    @Transactional
    public void giveAchievement(long userId, long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Достижение с ID %s не существует"
                        .formatted(achievementId)));

        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievement)
                .userId(userId)
                .build();

        userAchievementRepository.save(userAchievement);
    }

    @Transactional
    public void saveProgress(AchievementProgress progress) {
        achievementProgressRepository.save(progress);
    }

    public List<AchievementReadDto> getAllAchievements(AchievementFilterDto filterDto) {
        List<Achievement> achievements = achievementRepository.findAll();

        return achievements.stream()
                .filter(achievement -> achievementFilters.stream()
                        .anyMatch(filter -> filter.apply(Stream.of(achievement), filterDto).findFirst().isPresent()))
                .map(achievementMapper::toDto)
                .toList();
    }

    public List<AchievementReadDto> getAchievementsByUserId(long userId) {
        List<UserAchievement> userAchievements = userAchievementRepository.findByUserId(userId);

        return userAchievements.stream()
                .map(UserAchievement::getAchievement)
                .map(achievementMapper::toDto)
                .toList();
    }

    public AchievementReadDto getAchievementById(long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Достижение с ID " + achievementId + " не найдено"));

        return achievementMapper.toDto(achievement);
    }

    public List<AchievementProgressReadDto> getAchievementProgressByUserId(long userId) {
        List<AchievementProgress> achievementProgresses = achievementProgressRepository.findByUserId(userId);

        return achievementProgresses.stream()
                .map(achievementProgressMapper::toDto)
                .toList();
    }

    @Cacheable(value = "achievement", key = "#title")
    public Achievement getAchievementByTitle(String title) {
        return achievementRepository
                .findByTitleIgnoreCase(title)
                .orElseThrow(() -> new EntityNotFoundException("Нет ачивок с названием " + title));
    }
}
