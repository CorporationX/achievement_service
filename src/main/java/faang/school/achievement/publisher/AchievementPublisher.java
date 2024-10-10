package faang.school.achievement.publisher;

import faang.school.achievement.event.AchievementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementPublisher implements MessagePublisher<AchievementEvent> {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.data.redis.channels.achievement}")
    private String achievementChannel;

    @Override
    public void publish(AchievementEvent achievementEvent) {
        redisTemplate.convertAndSend(achievementChannel, achievementEvent);
    }
}
