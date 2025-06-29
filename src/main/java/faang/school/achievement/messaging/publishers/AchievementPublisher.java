package faang.school.achievement.messaging.publishers;

import faang.school.achievement.config.redis.RedisProperties;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.messaging.events.AchievementEvent;
import faang.school.achievement.model.UserAchievement;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementPublisher implements MessagePublisher {
    private static final String TOPIC_NAME = "achievement_channel";

    private final RedisProperties properties;
    private String topic;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AchievementMapper mapper;

    @PostConstruct
    private void init() {
        this.topic = properties.getChannels().get(TOPIC_NAME);
    }

    @Override
    public void publish(UserAchievement achievement) {
        Objects.requireNonNull(achievement, "UserAchievement to publish cannot be null");
        AchievementEvent event = mapper.toEvent(achievement);
        redisTemplate.convertAndSend(topic, event);
        log.info("Achievement event was published: {}", event);
    }
}
