package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.messaging.events.RecommendationEvent;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NiceGuyAchievementHandler implements EventHandler<RecommendationEvent> {

    private static final String ACHIEVEMENT_TITLE = "NICE GUY";

    private final AchievementCache cache;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementRepository achievementRepository;

    @Override
    @Transactional
    @Retryable(
            retryFor = OptimisticLockException.class,
            maxAttemptsExpression = "${spring.retry.max-attempts}",
            backoff = @Backoff(
                    delayExpression = "%{sprint.retry.delay}",
                    multiplierExpression = "%{sprint.retry.multiplier}")
    )
    public void handle(RecommendationEvent event) {
        AchievementDto achievementDto = cache.get(ACHIEVEMENT_TITLE);

        try {
            if (achievementProgressRepository.createOrIncrementAchievementProgress(
                    event.receiverId(),
                    achievementDto.id(),
                    achievementDto.requiredPoints()
            ) == 0) {
                log.debug("User with ID: {}, already has achievement: {}", event.receiverId(), ACHIEVEMENT_TITLE);
                return;
            }

            AchievementProgress achievementProgress = achievementProgressRepository.findByUserIdAndAchievementId(
                            event.receiverId(), achievementDto.id())
                    .orElseThrow(() -> new EntityNotFoundException(
                            String.format("User with ID: %s doesn't have any progress for achievement: %s",
                                    event.receiverId(), ACHIEVEMENT_TITLE)));

            if (achievementProgress.getCurrentPoints() == achievementDto.requiredPoints()) {
                UserAchievement userAchievement = new UserAchievement();
                userAchievement.setAchievement(achievementRepository.findByTitle(ACHIEVEMENT_TITLE).orElseThrow(
                        () -> new EntityNotFoundException(String.format("Achievement: %s was not found",
                                ACHIEVEMENT_TITLE))));
                userAchievement.setUserId(event.receiverId());
                userAchievementRepository.save(userAchievement);
            }
        } catch (OptimisticLockException ex) {
            log.warn("Optimistic lock failure: {}", ex.getMessage());
            throw ex;
        }
    }
}