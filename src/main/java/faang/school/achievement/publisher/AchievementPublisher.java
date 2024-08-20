package faang.school.achievement.publisher;

import faang.school.achievement.dto.PublishedUserAchievementDto;
import faang.school.achievement.mapper.publisher.PublishedUserAchievementMapper;
import faang.school.achievement.model.UserAchievement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;
    private final PublishedUserAchievementMapper publishedUserAchievementMapper;

    public void publish(UserAchievement userAchievement) {
        PublishedUserAchievementDto publishedUserAchievementDto =
                publishedUserAchievementMapper.toDto(userAchievement);
        redisTemplate.convertAndSend(channelTopic.getTopic(), publishedUserAchievementDto);
    }
}
