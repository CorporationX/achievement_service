package faang.school.achievement.service.achievement;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultAchievementService implements AchievementService {

    private final AchievementRepository achievementRepository;

    @Override
    @Transactional
    public Achievement getAchievementById(long achievementId) {
        return achievementRepository.findById(achievementId)
                .orElseThrow(() -> new NoSuchElementException("Achievement was not found"));
    }
}