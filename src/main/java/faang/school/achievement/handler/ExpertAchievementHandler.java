package faang.school.achievement.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementType;
import faang.school.achievement.dto.event.CommentEvent;
import faang.school.achievement.exception.JsonConvertException;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpertAchievementHandler implements EventHandler<CommentEvent> {
    private final AchievementService achievementService;
    private final CacheService cacheService;
    private final ObjectMapper objectMapper;

    @Override
    public Class<?> getInstance() {
        return CommentEvent.class;
    }

    @Async("fixedThreadPool")
    @Override
    public void handle(CommentEvent event) {
        log.info("Started checking conditions for '{}' achievement for userId={}",
                 AchievementType.EXPERT, event.commentAuthorId());
        String json = cacheService.getAchievement(AchievementType.EXPERT.name());
        try {
            AchievementDto achievement = objectMapper.readValue(json, AchievementDto.class);
            long userId = event.commentAuthorId();
            long achievementId = achievement.id();

            if (achievementService.hasAchievement(userId, achievementId)) {
                log.info("User userId={} already has '{}' achievement", userId, AchievementType.EXPERT);
                return;
            }

            achievementService.createProgressIfNecessary(userId, achievementId);
            AchievementProgress progress = achievementService.getProgress(userId, achievementId);
            long newCurrentPoints = achievementService.incrementAndGetPointsById(progress.getId());
            log.info("'{}' achievement progress increased for userId={} to {} points",
                     AchievementType.EXPERT, userId, newCurrentPoints);

            if (newCurrentPoints >= achievement.points()) {
                achievementService.giveAchievement(achievement.id(), userId);
                log.info("User userId={} got '{}' achievement", userId, AchievementType.EXPERT);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new JsonConvertException("Failed to map json to object. Json: {}", json);
        }
    }
}
