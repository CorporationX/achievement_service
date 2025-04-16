package faang.school.achievement.service;

import faang.school.achievement.exception.NotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService { //TODO сделать интерфейс

    private final AchievementRepository achievementRepository;

    public Achievement getAchievementById(long achievementId) {
        return achievementRepository.findById(achievementId)
                .orElseThrow(() -> new NotFoundException("Achievement was not found"));
    }
}