package faang.school.achievement.handler.kafka;

import faang.school.achievement.dto.TeamEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagerAchievementHandler implements KafkaEventHandler {
  private final AchievementService achievementService;
  private static final String ACHIEVEMENT_TITLE = "TeamCreator";

  @Async
  @Override
  public void handle(TeamEvent event) {
    Long userId = event.getAuthorId();
    Achievement achievement = achievementService.getAchievement(ACHIEVEMENT_TITLE);

    if (achievementService.hasAchievement(userId, achievement)) {
      return;
    }

    AchievementProgress progress = achievementService.createProgressIfNecessary(userId, achievement);
    progress.increment();

    if (progress.getCurrentPoints() >= achievement.getPoints()) {
      achievementService.giveAchievement(userId, achievement);
    }
  }
}