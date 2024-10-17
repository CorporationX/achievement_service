package faang.school.achievement.publisher.achievement;

import faang.school.achievement.dto.achievement.UserAchievementDto;
import faang.school.achievement.publisher.MessagePublisher;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@Builder
@Slf4j
@RequiredArgsConstructor
public class UserAchievementPublisher implements MessagePublisher<UserAchievementDto> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic achievementChannelTopic;

    @Override
    public void publish(UserAchievementDto message) {
        redisTemplate.convertAndSend(achievementChannelTopic.getTopic(), message);
        log.debug("Sent message with id : {} in {} channel", message.getId(),
                achievementChannelTopic.getTopic());
    }
}
