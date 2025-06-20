package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementProgressRecord;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementCode;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.redis.AchievementCache;
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
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementMapper achievementMapper;
    private final AchievementCache achievementCache;

    @Transactional
    public AchievementProgress saveAchievementProgress(AchievementProgressRecord progressRecord) {
        log.info("Calling saveAchievementProgress, record: {}", progressRecord.achievement().getTitle());
        Optional<AchievementProgress> loadedAchievementProgress =
                achievementProgressRepository.findByUserIdAndAchievementId(progressRecord.userId(),
                        progressRecord.achievement().getId());
        AchievementProgress progress;
        if (loadedAchievementProgress.isPresent()) {
            progress = loadedAchievementProgress.get();
            long currentPoints = progress.getCurrentPoints();
            progress.setCurrentPoints(currentPoints + progressRecord.currentPoints());
        } else {
            AchievementProgress achievementProgress = achievementMapper.toAchievement(progressRecord);
            progress = achievementProgressRepository.save(achievementProgress);
        }
        return progress;
    }

    @Transactional
    public boolean assignAchievementIfCompleted(AchievementProgressRecord achievementProgressRecord) {
        log.info("Assigning an achievement if completed, achievement progress record: {}",
                achievementProgressRecord.achievement().getTitle());
        long currentPoints = achievementProgressRecord.currentPoints();
        Long achievementPoints = achievementRepository.findPointsByTitle(achievementProgressRecord.achievement().getTitle());
        if (currentPoints >= achievementPoints) {
            assignUserAchievement(achievementProgressRecord);
            return true;
        }
        return false;
    }

    private void assignUserAchievement(AchievementProgressRecord achievementProgressRecord) {
        log.info("Assigning a completed achievement to a user");
        userAchievementRepository.save(achievementMapper.toUserAchievement(achievementProgressRecord));
    }

    public List<Achievement> getAchievements() {
        return achievementCache.getAchievements();
    }

    public Achievement getAchievementByCode(AchievementCode achievementCode) {
        return achievementCache.getAchievementByCode(achievementCode);
    }
}