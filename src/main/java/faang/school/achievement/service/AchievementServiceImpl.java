package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.exception.AchievementException;
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
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementMapper achievementMapper;
    private final UserAchievementMapper userAchievementMapper;
    private final AchievementProgressMapper achievementProgressMapper;

    public List<AchievementDto> getAllAchievements() {
        List<Achievement> achievements = StreamSupport.stream(achievementRepository.findAll().spliterator(), false)
                        .collect(Collectors.toList());
        achievements.sort(Comparator.comparing(Achievement::getRarity));

        return achievements.stream()
                .map(achievementMapper::toDto)
                .toList();
    }

    @Override
    public List<UserAchievementDto> getAllUserAchievements(Long userId) {
        return userAchievementRepository.findByUserId(userId).stream()
                .map(userAchievementMapper::toDto)
                .toList();
    }

    @Override
    public AchievementDto getAchievement(Long achievementId) {
        return achievementRepository.findById(achievementId)
                .map(achievementMapper::toDto)
                .orElseThrow(() -> new AchievementException("no achievement found with id " + achievementId));
    }

    @Override
    public List<AchievementProgressDto> getUserAchievementProgress(Long userId) {
        return achievementProgressRepository.findByUserId(userId).stream()
                .map(achievementProgressMapper::toDto)
                .toList();
    }
}
