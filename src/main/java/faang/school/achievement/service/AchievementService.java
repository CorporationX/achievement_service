package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.event.AchievementEvent;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {

    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementRepository repository;
    private final AchievementMapper achievementMapper;
    private final AchievementProgressMapper achievementProgressMapper;
    private final AchievementPublisher achievementPublisher;
    private final AchievementProgressRepository progressRepository;
    private final AchievementCache achievementCache;

    @Transactional(readOnly = true)
    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional(readOnly = true)
    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementIdWithLock(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Прогресс по достижению с id %s не существует", achievementId)));
    }

    public void giveAchievementIfNecessary(long userId, Achievement achievement) {
        userAchievementRepository.giveAchievementIfNecessary(userId, achievement.getId());
        achievementPublisher.publishAchievement(createEvent(achievement));
    }

    @Transactional
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public void saveProgress(AchievementProgress progress) {
        achievementProgressRepository.save(progress);
    }

    public List<AchievementDto> getAllAchievements(String name, String description, Rarity rarity) {
        List<Achievement> achievements = repository.findByFilters(name, description, rarity);
        return achievementMapper.toDtoList(achievements);
    }

    public List<UserAchievementDto> getUserAchievements(Long userId) {
        return achievementMapper.toUserAchievementDtoList(userAchievementRepository.findByUserId(userId));
    }

    public AchievementDto getAchievementById(Long id) {
        Achievement achievement = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Достижение с ID=" + id + " не найдено."));
        return achievementMapper.toDto(achievement);
    }

    public List<AchievementProgressDto> getUserPendingAchievements(Long userId) {
        return achievementProgressMapper.toDtoList(achievementProgressRepository.findByUserId(userId));
    }

    private AchievementEvent createEvent(Achievement achievement) {
        return AchievementEvent.builder()
                .id(achievement.getId())
                .title(achievement.getTitle())
                .description(achievement.getDescription())
                .rarity(achievement.getRarity())
                .build();
    }
}