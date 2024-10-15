package faang.school.achievement.service.publisher;

import faang.school.achievement.dto.event.AchievementEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementEventPublisher implements MessagePublisher<AchievementEvent> {

    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;

    @Override
    public void publish(AchievementEvent message) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
        log.info("Message was send {}, in topic - {}", message, channelTopic.getTopic());
    }
}
