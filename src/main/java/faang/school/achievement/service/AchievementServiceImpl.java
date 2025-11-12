package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;

    @Override
    public List<AchievementDto> getFilteredAchievements(String title, String description, String rarity) {
        List<Achievement> achievements =
                StreamSupport.stream(achievementRepository.findAll().spliterator(), false)
                        .filter(ach -> title == null
                                || ach.getTitle().toLowerCase().contains(title.toLowerCase()))
                        .filter(ach -> description == null
                                || ach.getDescription().toLowerCase().contains(description.toLowerCase()))
                        .filter(ach -> rarity == null
                                || ach.getRarity().name().toLowerCase().contains(rarity.toLowerCase())).toList();

        log.info("Found {} achievements matching filters", achievements.size());
        return achievements.stream()
                .map(achievementMapper::toDto)
                .toList();
    }

    @Override
    public List<AchievementDto> getUserAchievements(Long userId) {
        List<UserAchievement> userAchievements = userAchievementRepository.findByUserId(userId);
        List<AchievementDto> achievementDtos = userAchievements.stream()
                .map(ua -> ua.getAchievement())
                .map(achievementMapper::toDto)
                .toList();

        log.info("Found {} achievements for user {}", achievementDtos.size(), userId);
        return achievementDtos;
    }

    @Override
    public AchievementDto getAchievementById(Long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Achievement not found with id: " + achievementId));

        log.info("Successfully fetched achievement: {}", achievement.getTitle());
        return achievementMapper.toDto(achievement);
    }

    @Override
    public List<AchievementProgressDto> getUnearnedAchievementsWithProgress(Long userId) {
        Set<Long> earnedAchievementIds = userAchievementRepository.findByUserId(userId)
                .stream()
                .map(ua -> ua.getAchievement().getId())
                .collect(Collectors.toSet());

        List<AchievementProgress> allProgress = achievementProgressRepository.findByUserId(userId);
        List<AchievementProgressDto> progressDtos = allProgress.stream()
                .filter(progress -> !earnedAchievementIds.contains(progress.getAchievement().getId()))
                .map(progress -> new AchievementProgressDto(
                        progress.getAchievement().getId(),
                        progress.getUserId(),
                        progress.getCurrentPoints(),
                        extractTargetFromDescription(progress.getAchievement().getDescription())))
                .toList();

        log.info("Found {} unearned achievements with progress for user {}", progressDtos.size(), userId);
        return progressDtos;
    }

    private long extractTargetFromDescription(String description) {
        if (description == null) return 1L;

        // Удаляем все пробелы и ищем первое число
        String cleaned = description.replaceAll("\\s+", "");
        return java.util.regex.Pattern.compile("\\d+")
                .matcher(cleaned)
                .results()
                .findFirst()
                .map(match -> Long.parseLong(match.group()))
                .orElse(1L);
    }
}
