package faang.school.achievement.service.achievement;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "app.service.achievement",
        havingValue = "default",
        matchIfMissing = true
)
public class DefaultAchievementService implements AchievementService {

    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;

    @Transactional(readOnly = true)
    @Override
    public Achievement getAchievement(long achievementId) {
        log.debug("Starting obtain achievement {}...", achievementId);
        return achievementRepository.findById(achievementId)
                .orElseThrow(() -> new AchievementNotFoundException(
                        String.format("Achievement with %d ID not found", achievementId)
                ));
    }

    @Transactional(readOnly = true)
    @Override
    public List<AchievementDto> getAchievements() {
        log.debug("Starting obtain achievement list ...");
        return achievementMapper.toDtoList(achievementRepository.findAll());
    }
}
