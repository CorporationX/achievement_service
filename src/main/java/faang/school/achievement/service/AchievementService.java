package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.exception.EmptyFilterException;
import faang.school.achievement.exception.ExceptionMessage;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementMapper achievementMapper;

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

    private List<AchievementDto> convertToAchievementDto(List<Achievement> achievements) {
        if (achievements.isEmpty()) {
            return Collections.emptyList();
        }

        List<AchievementDto> achievementDtos = new ArrayList<>();
        for (Achievement achievement : achievements) {
            achievementDtos.add(achievementMapper.toDto(achievement));
        }

        return achievementDtos;
    }
}
