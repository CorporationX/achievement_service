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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CollectorAchievementHandler implements EventHandler<String> {

    private static final int REQUIRED_GOALS = 100;

    private final AchievementService achievementService;
    private final UserAchievementRepository userAchievementRepository;
    private final ObjectMapper objectMapper;

    @Async
    @Override
    public void handle(String event) {
        GoalSetEvent eventAsObject;
        try {
            eventAsObject = objectMapper.readValue(event, GoalSetEvent.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        Achievement achievement = achievementService.getAchievement(AchievementStatus.COLLECTOR.getStatusTitle());
        if (userAchievementRepository.existsByUserIdAndAchievementId(
            eventAsObject.getUserId(),
            achievement.getId()
        )) {
            return;
        }

        achievementService.createProgressIfNecessary(eventAsObject.getUserId(), achievement);
        AchievementProgress progress = achievementService.getProgress(eventAsObject.getUserId(), achievement.getId());
        AchievementProgress updatedProgress = achievementService.increaseAchievementProgress(progress, 1);
        if (updatedProgress.getCurrentPoints() >= REQUIRED_GOALS) {
            achievementService.giveAchievement(eventAsObject.getUserId(), achievement);
        }
    }
}
