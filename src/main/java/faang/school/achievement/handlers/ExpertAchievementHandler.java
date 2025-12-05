package faang.school.achievement.handlers;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.CommentEventDto;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.TransactionalLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static faang.school.achievement.utils.Utils.stringFormatting;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpertAchievementHandler implements TimedEventHandler<CommentEventDto> {
    private static final String ACHIEVEMENT_NAME = "EXPERT";
    private static final long HANDLER_EXECUTION_TIME_MS = 5000;
    private final AchievementService achievementService;
    private final TransactionalLockService lockService;
    @Qualifier("AchievementCache")
    private final AchievementCache achievementCache;

    @Override
    public void handle(CommentEventDto event) {

        lockService.runWithTransactionAndLock(event.authorId(), () -> {
            long authorId = event.authorId();
            log.info("Starting achievement progress update for user '{}' for a new comment.", authorId);

            log.info("Fetching achievement details for '{}'.", ACHIEVEMENT_NAME);
            Achievement achievement = achievementCache.getByTitle(ACHIEVEMENT_NAME).orElseThrow(
                () -> new EntityNotFoundException(stringFormatting("There is no {} achievement",
                                                                   ACHIEVEMENT_NAME)));

            log.info("Checking if user '{}' already has the achievement '{}'.", authorId, achievement.getId());
            if (achievementService.hasAchievement(authorId, achievement.getId())) {
                log.info("User '{}' already has achievement '{}'. Skipping progress update.", authorId,
                         ACHIEVEMENT_NAME);
                return;
            }

            log.info("Ensuring achievement progress record exists for user '{}' and achievement '{}'.", authorId,
                     achievement.getId());
            achievementService.createProgressIfNecessary(authorId, achievement.getId());

            log.info("Retrieving current progress for user '{}'.", authorId);
            AchievementProgress progress = achievementService.getProgress(authorId, achievement.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find progress for a user that should have it."));

            long currentPoints = progress.getCurrentPoints() + 1;
            progress.setCurrentPoints(currentPoints);
            log.info("Incrementing progress points for user '{}'. New points: {}", authorId, currentPoints);

            log.info("Saving updated progress for user '{}'.", authorId);
            achievementService.saveProgress(progress);

            log.info("Checking if user '{}' has met the threshold of {} points.", authorId, achievement.getPoints());
            if (currentPoints >= achievement.getPoints()) {
                achievementService.giveAchievement(authorId, achievement.getId());
                log.info("Achievement '{}' granted to user '{}'.", ACHIEVEMENT_NAME, authorId);
            } else {
                log.debug("User '{}' progress is now {}/{}.", authorId, currentPoints, achievement.getPoints());
            }
        });
    }

    @Override
    public long getHandlerExecutionTime() {
        return HANDLER_EXECUTION_TIME_MS;
    }
}