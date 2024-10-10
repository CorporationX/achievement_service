package faang.school.achievement.event.handler;

import faang.school.achievement.event.AchievementEvent;
import faang.school.achievement.event.Event;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.publisher.AchievementPublisher;
import faang.school.achievement.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public abstract class EventHandler<T extends Event> {

    private final CacheService<String> cacheService;
    private final AchievementPublisher achievementPublisher;

    @Value("${spring.data.redis.life-time-minutes}")
    private int lifeTimeMinutes;

    @Transactional
    public void handleEventIfNotProcessed(T event) {
        String key = generateKey(event.getEventTime());
        if (!cacheService.exists(key)) {
            handleEvent(event);
            cacheService.put(key, key, Duration.ofMinutes(lifeTimeMinutes));
        } else {
            log.info("Event already processed, ignore it");
        }
    }

    protected void publishNotification(long userId, Achievement achievement) {
        AchievementEvent achievementEvent = new AchievementEvent(
                LocalDateTime.now(),
                userId,
                achievement.getId(),
                achievement.getTitle(),
                achievement.getDescription()
        );
        achievementPublisher.publish(achievementEvent);
    }

    private String generateKey(LocalDateTime localDateTime) {
        return "%s:%s:%s".formatted(getHandlerClass().getSimpleName(), getEventClass().getSimpleName(), localDateTime);
    }

    protected abstract void handleEvent(T event);
    protected abstract Class<T> getEventClass();
    protected abstract Class<?> getHandlerClass();
}
