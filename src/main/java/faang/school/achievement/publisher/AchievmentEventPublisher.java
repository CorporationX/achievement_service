package faang.school.achievement.publisher;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import faang.school.achievement.dto.AnalyticEventDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AchievmentEventPublisher implements EventPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publish(AnalyticEventDto message) {
        redisTemplate.convertAndSend(getTopic(),message);
    }

    private String getTopic() {
        return "achievement_channel";
    }
}
