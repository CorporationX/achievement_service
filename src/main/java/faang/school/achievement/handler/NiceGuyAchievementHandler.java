package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.events.RecommendationEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Component
@RequiredArgsConstructor
public class NiceGuyAchievementHandler implements EventHandler<RecommendationEvent> {
    private static final String ACHIEVEMENT_NAME = "NICE GUY";
    private final AchievementService achievementService;
    private final AchievementCache achievementCache;
    private final AchievementRepository achievementRepository;

    @Override
    @Transactional
    public void handle(RecommendationEvent recommendationEvent) {
        AchievementDto achievementDto = achievementCache.get(ACHIEVEMENT_NAME).orElseThrow(
                () -> new NoSuchElementException("No achievement exists with such title."));

        if (achievementService.hasAchievement(recommendationEvent.receiverId(), achievementDto.id())) {
            return;
        }

        achievementService.createProgressIfNecessary(recommendationEvent.receiverId(), achievementDto.id());

        AchievementProgress achievementProgress = achievementService.getProgress(
                recommendationEvent.receiverId(),
                achievementDto.id()
        ).orElseThrow(() -> new NoSuchElementException("No progress present for such user and achievement"));

        achievementProgress.increment();

        if (achievementProgress.getCurrentPoints() == achievementDto.requiredPoints()) {
            UserAchievement userAchievementToGive = new UserAchievement();
            Achievement achievement = achievementRepository.findById(achievementDto.id()).orElseThrow(
                    () -> new NoSuchElementException("No achievement with such ID was found"));
            userAchievementToGive.setAchievement(achievement);
            userAchievementToGive.setUserId(recommendationEvent.receiverId());
            achievementService.giveAchievement(userAchievementToGive);
        }
    }
}