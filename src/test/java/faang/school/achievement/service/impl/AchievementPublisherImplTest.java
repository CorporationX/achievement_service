package faang.school.achievement.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.redis.RedisProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

@ExtendWith(MockitoExtension.class)
class AchievementPublisherImplTest {

    @InjectMocks
    private AchievementPublisherImpl achievementPublisher;
    @Mock
    private RedisTemplate redisTemplate;
    @Spy
    private ObjectMapper objectMapper;
    //@Spy
    private RedisProperties redisProperties;

    @BeforeEach
    void setUp() {
        RedisProperties.Channel channel = new RedisProperties.Channel("achievementChannel",
                "followerChannel", "cacheUpdatesChannel");
        RedisProperties.Cache cache = new RedisProperties.Cache(60);
        //redisProperties = new RedisProperties(6379, "localhost", channel, cache);
        redisProperties = Mockito.spy(new RedisProperties(6379, "localhost", channel, cache));
    }

    @Test
    void testPublishMessage() throws JsonProcessingException {
/*        AchievementEvent achievementEvent = AchievementEvent.builder()
                .userId(1L)
                .achievementId(9L)
                .build();
        String json = objectMapper.writeValueAsString(achievementEvent);
        //Mockito.when(redisProperties.channel().achievement()).thenReturn("achievement_channel");
        //Mockito.doNothing().when(redisTemplate).convertAndSend(redisProperties.channel().achievement(), json);
        achievementPublisher.publishMessage(achievementEvent);
        Mockito.verify(redisTemplate,
                Mockito.times(1)).convertAndSend(redisProperties.channel().achievement(), json);
*/

    }
}