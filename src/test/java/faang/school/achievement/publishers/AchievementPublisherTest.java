package faang.school.achievement.publishers;

import faang.school.achievement.mapper.AchievementMapperImpl;
import faang.school.achievement.messaging.events.AchievementEvent;
import faang.school.achievement.messaging.publishers.AchievementPublisher;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AchievementPublisherTest {
    private final static long ENTITY_ID = 1L;
    private final static long USER_ID = 1L;
    private final static String ACHIEVEMENT_TITLE = "testTitle";
    private static final String ACTUAL_TOPIC_VALUE = "achievements-test-topic";

    private UserAchievement userAchievement;
    private AchievementEvent expectedEvent;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Spy
    private AchievementMapperImpl mapper;

    @InjectMocks
    private AchievementPublisher publisher;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(publisher, "topic", ACTUAL_TOPIC_VALUE);

        Achievement achievement = Achievement.builder()
                .id(ENTITY_ID)
                .title(ACHIEVEMENT_TITLE)
                .build();

        userAchievement = UserAchievement.builder()
                .id(ENTITY_ID)
                .userId(USER_ID)
                .achievement(achievement)
                .build();

        expectedEvent = new AchievementEvent(USER_ID, ACHIEVEMENT_TITLE);
    }

    @Test
    void testPublish_whenValidInput_thenSendEventToRedis() {
        publisher.publish(userAchievement);
        verify(redisTemplate).convertAndSend(eq(ACTUAL_TOPIC_VALUE), eq(expectedEvent));
    }

    @Test
    void testPublish_whenInputIsNull_thenThrowsExceptionAndDoesNotCallRedis() {
        assertThrows(NullPointerException.class, () -> publisher.publish(null));
        verify(redisTemplate, never()).convertAndSend(anyString(), any());
    }
}
