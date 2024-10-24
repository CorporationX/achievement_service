package faang.school.achievement.publish.achievement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.redis.RedisProperties;
import faang.school.achievement.dto.achievement.UserAchievementEventDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publish.publisher.AchievementEventPublisher;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AchievementEventPublisherTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private RedisProperties redisProperties;

    @Mock
    private AchievementMapper achievementMapper;

    private ObjectMapper objectMapper = new ObjectMapper();
    private AchievementEventPublisher achievementEventPublisher;

    @Captor
    ArgumentCaptor<String> captor;

    public AchievementEventPublisherTest() {
        MockitoAnnotations.openMocks(this);
        achievementEventPublisher = new AchievementEventPublisher(objectMapper, redisTemplate, redisProperties, achievementMapper);
    }

    @Test
    public void testPublishNewUserAchievementEvent() throws JsonProcessingException {
        // Arrange
        UserAchievement newUserAchievement = new UserAchievement(1L, new Achievement(), 2L, LocalDateTime.now(), LocalDateTime.now());
        UserAchievementEventDto achievementEventDto = UserAchievementEventDto.builder()
                .userId(newUserAchievement.getUserId())
                .achievementId(2L)
                .rarity(Rarity.COMMON)
                .title("Test Title")
                .build();

        String achievementEventChannelName = "testChannel";
        when(redisProperties.getAchievementEventChannelName()).thenReturn(achievementEventChannelName);
        when(achievementMapper.toUserAchievementEventDto(newUserAchievement)).thenReturn(achievementEventDto);
        achievementEventPublisher.publishNewUserAchievementEventToBroker(newUserAchievement);
        verify(redisTemplate, times(1)).convertAndSend(eq(achievementEventChannelName), captor.capture());
        String expectedJson = objectMapper.writeValueAsString(achievementEventDto);
        assertThat(captor.getValue()).isEqualTo(expectedJson);
    }
}
