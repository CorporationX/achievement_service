package faang.school.achievement.handlers;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.BaseEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.TransactionalLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;

import static faang.school.achievement.utils.Utils.stringFormatting;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventHandler<T extends BaseEvent> implements TimedEventHandler<T> {
    private final AchievementService achievementService;
    private final String achievementName;
    private final AchievementCache achievementCache;
    private final TransactionalLockService lockService;

    @Override
    public void handle(T event) {
        lockService.runWithTransactionAndLock(event.authorId(), () -> {
            long authorId = event.authorId();
            log.info("Starting achievement progress update for user '{}' for a new comment.", authorId);

            log.info("Fetching achievement details for '{}'.", achievementName);
            Achievement achievement = achievementCache.getByTitle(achievementName).orElseThrow(
                () -> new NoSuchElementException(stringFormatting("There is no {} achievement",
                                                                  achievementName)));

            log.info("Checking if user '{}' already has the achievement '{}'.", authorId, achievement.getId());
            if (achievementService.hasAchievement(authorId, achievement.getId())) {
                log.info("User '{}' already has achievement '{}'. Skipping progress update.", authorId,
                         achievementName);
                return;
            }

            log.info("Ensuring achievement progress record exists for user '{}' and achievement '{}'.", authorId,
                     achievement.getId());
            achievementService.createProgressIfNecessary(authorId, achievement.getId());

            log.info("Retrieving current progress for user '{}'.", authorId);
            AchievementProgress progress = achievementService.getProgress(authorId, achievement.getId())
                .orElseThrow(() -> new NoSuchElementException("Cannot find progress for a user that should have it."));

            long currentPoints = progress.getCurrentPoints() + 1;
            progress.setCurrentPoints(currentPoints);
            log.info("Incrementing progress points for user '{}'. New points: {}", authorId, currentPoints);

            log.info("Saving updated progress for user '{}'.", authorId);
            achievementService.saveProgress(progress);

            log.info("Checking if user '{}' has met the threshold of {} points.", authorId, achievement.getPoints());
            if (currentPoints >= achievement.getPoints()) {
                achievementService.giveAchievement(authorId, achievement.getId());
                log.info("Achievement '{}' granted to user '{}'.", achievementName, authorId);
            } else {
                log.debug("User '{}' progress is now {}/{}.", authorId, currentPoints, achievement.getPoints());
            }
        });
    }
}