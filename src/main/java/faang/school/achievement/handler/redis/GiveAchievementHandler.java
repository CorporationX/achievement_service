package faang.school.achievement.handler.redis;

import faang.school.achievement.dto.event.GiveAchievementEvent;
import faang.school.achievement.mapper.EventMapper;
import faang.school.achievement.properties.AchievementProperties;
import faang.school.achievement.redis.RedisEventPublisher;
import faang.school.achievement.redis.event.AchievementRedisEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GiveAchievementHandler {
  private final RedisEventPublisher publisher;
  private final AchievementProperties achievementProperties;
  private final EventMapper eventMapper;

  @EventListener
  @Async
  public void handleGiveAchievement(GiveAchievementEvent event) {
    log.info("Handling give achievement event: {}", event);

    AchievementRedisEvent redisEvent = eventMapper.toAchievementRedisEvent(event);
    publisher.publish(redisEvent, achievementProperties.getRedis().getGiveAchievementChannel());
    log.info("Give achievement event sent to Redis: {}", redisEvent);
  }
}
