package faang.school.achievement.kafka.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.kafka.AchievementStatus;
import faang.school.achievement.kafka.EventHandler;
import faang.school.achievement.kafka.GoalSetEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CollectorAchievementHandler implements EventHandler<String> {

    @Value("${achievement-service.required-goal-count}")
    private int REQUIRED_GOALS;

    private final AchievementService achievementService;
    private final UserAchievementRepository userAchievementRepository;
    private final ObjectMapper objectMapper;

    @Async
    @Override
    public void collectEvent(String event) {
        GoalSetEvent eventAsObject = readGoalSetEvent(event);
        Achievement achievement = achievementService.getAchievement(AchievementStatus.COLLECTOR.getStatusTitle());
        boolean isGoalExist = userAchievementRepository.existsByUserIdAndAchievementId(
                eventAsObject.getUserId(),
                achievement.getId()
        );
        if (isGoalExist) {
            log.warn(
                "this goal is already exists by achievement id {} and user id {}",
                achievement.getId(),
                eventAsObject.getUserId()
            );
            return;
        }

        achievementService.createProgressIfNecessary(eventAsObject.getUserId(), achievement);
        AchievementProgress progress = achievementService.getProgress(eventAsObject.getUserId(), achievement.getId());
        AchievementProgress updatedProgress = achievementService.increaseAchievementProgress(progress, 1);

        if (updatedProgress.getCurrentPoints() >= REQUIRED_GOALS) {
            achievementService.giveAchievement(eventAsObject.getUserId(), achievement);
        }
    }

    private GoalSetEvent readGoalSetEvent(String event) {
        try {
            return objectMapper.readValue(event, GoalSetEvent.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
