package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.exception.EmptyFilterException;
import faang.school.achievement.exception.ExceptionMessage;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.mapper.UserAchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementMapper achievementMapper;
    private final AchievementProgressMapper achievementProgressMapper;
    private final UserAchievementMapper userAchievementMapper;

    public List<AchievementDto> findAll() {
        List<Achievement> achievements = (List<Achievement>) achievementRepository.findAll();

        return convertToAchievementDto(achievements);
    }

    public List<AchievementDto> findFilteredAchievements(AchievementFilterDto filter) {
        if (filter.title() == null && filter.description() == null && filter.rarity() == null) {
            throw new EmptyFilterException(ExceptionMessage.EMPTY_FILTER);
        }
        List<Achievement> achievements = achievementRepository.findAchievementByFilters(
                filter.title(),  filter.description(), filter.rarity());

        return convertToAchievementDto(achievements);
    }

    public List<UserAchievementDto> findAchievementsByUserId(long userId) {
        List<UserAchievement> achievements = userAchievementRepository.findByUserId(userId);

        return achievements.stream()
                .map(userAchievementMapper::toDto)
                .toList();
    }

    public AchievementDto findById(long id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new AchievementNotFoundException(ExceptionMessage.ACHIEVEMENT_NOT_FOUND, id));

        return achievementMapper.toDto(achievement);
    }

    public List<AchievementProgressDto> findProcessingAchievementsByUserId(long userId) {
        List<AchievementProgress> achievements = achievementProgressRepository.findByUserId(userId);

        return achievements.stream()
                .map(achievementProgressMapper::toDto)
                .toList();
    }

    public Optional<Achievement> getAchievementByTitle(String title) {
        return achievementRepository.findByTitle(title);
    }

    public boolean hasAchievementForUser(Long userId, Long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void createProgressIfNecessary(Long userId, Long achievementId) {
        if (getAchievementProgress(userId, achievementId).isEmpty()) {
            achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
        }
    }

    public Optional<AchievementProgress> getAchievementProgress(Long userId, Long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId);
    }

    public void giveAchievementForUser(Long userId, Achievement achievement) {
        userAchievementRepository.save(createUserAchievement(userId, achievement));
    }

    @Transactional
    public int incrementProgress(Long progressId) {
        return achievementProgressRepository.increment(progressId);
    }

    private UserAchievement createUserAchievement(Long userId, Achievement achievement) {
        return UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();
    }

    private List<AchievementDto> convertToAchievementDto(List<Achievement> achievements) {
        return achievements.stream()
                .map(achievementMapper::toDto)
                .toList();
    }
}
