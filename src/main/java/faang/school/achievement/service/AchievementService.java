package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementMapper achievementMapper;
    private final AchievementProgressMapper achievementProgressMapper;

    public List<AchievementDto> getAllAchievements(String name, String description, Rarity rarity) {
        List<Achievement> achievements = achievementRepository.findAll();

        return achievementMapper.toDtoList(
                achievements.stream()
                        .filter(a -> (name == null || a.getTitle().contains(name)) &&
                                (description == null || a.getDescription().contains(description)) &&
                                (rarity == null || a.getRarity() == rarity))
                        .toList()
        );
    }

    public List<UserAchievementDto> getUserAchievements(Long userId) {
        return achievementMapper.toUserAchievementDtoList(userAchievementRepository.findByUserId(userId));
    }

    public AchievementDto getAchievementById(Long id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Достижение с ID=" + id + " не найдено."));
        return achievementMapper.toDto(achievement);
    }

    public List<AchievementProgressDto> getUserPendingAchievements(Long userId) {
        return achievementProgressMapper.toDtoList(achievementProgressRepository.findByUserId(userId));
    }
}