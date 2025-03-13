package faang.school.achievement.handler.kafka;

import faang.school.achievement.dto.TeamEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class ManagerAchievementHandler implements KafkaEventHandler<TeamEvent> {

  private static final String ACHIEVEMENT_TITLE = "TeamCreator";

  private final AchievementService achievementService;

  @Async
  @Override
  public void handle(TeamEvent event) {
    Long userId = event.getAuthorId();
    Achievement achievement = achievementService.getAchievement(ACHIEVEMENT_TITLE);

    if (achievementService.hasAchievement(userId, achievement)) {
      log.info("User with id: {} is already has achievement {}", userId, achievement.getTitle());
      return;
    }

    AchievementProgress progress = achievementService.createProgressIfNecessary(userId, achievement);
    progress.increment();

    if (progress.getCurrentPoints() >= achievement.getPoints()) {
      achievementService.giveAchievement(userId, achievement);
    }
  }
}