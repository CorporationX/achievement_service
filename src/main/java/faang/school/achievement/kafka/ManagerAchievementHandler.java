package faang.school.achievement.kafka;

import faang.school.achievement.dto.TeamEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagerAchievementHandler implements KafkaEventHandler {
  private final AchievementService achievementService;
  private static final String ACHIEVEMENT_TITLE = "TeamCreator";

  @Override
  @Async
  public void handle(TeamEvent event) {
    Long userId = event.getAuthorId();
    Achievement achievement = getAchievementFromCache();

    if (achievementService.hasAchievement(userId, achievement.getId())) {
      return;
    }

    AchievementProgress progress = achievementService.createProgressIfNecessary(userId, achievement.getId());
    progress = achievementService.getProgress(userId, achievement.getId());
    progress.increment();

    if (progress.getCurrentPoints() >= achievement.getPoints()) {
      achievementService.giveAchievement(userId, achievement.getId());
    }
  }

  @Cacheable("achievements")
  private Achievement getAchievementFromCache() {
    return achievementService.findByTitle(ACHIEVEMENT_TITLE)
        .orElseThrow(() -> new IllegalStateException("Achievement not found"));
  }
}