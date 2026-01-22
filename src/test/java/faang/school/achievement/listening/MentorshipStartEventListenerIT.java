package faang.school.achievement.listening;

import com.redis.testcontainers.RedisContainer;
import faang.school.achievement.handling.SenseiAchievementHandler;
import faang.school.achievement.model.event.MentorshipStartEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class MentorshipStartEventListenerIT {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.data.redis.channel.mentorship}")
    String mentorshipTopic;

    private final LocalDateTime TIMESTAMP = LocalDateTime.now();

    private static final long MENTOR_ID = 10L;

    private static final long MENTEE_ID = 9L;

    @SpyBean
    SenseiAchievementHandler handler;

    @Container
    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis/redis-stack:latest"));

    @Container
    public static PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:13.6");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
    }

    @Test
    public void eventIsDeliveredToHandler() {
        publishEvent();

        await().untilAsserted(() ->
                verify(handler).handleAchievement(any())
        );
    }

    @Test
    public void eventIsDeserialized() {
        publishEvent();

        ArgumentCaptor<MentorshipStartEvent> captor = ArgumentCaptor.forClass(MentorshipStartEvent.class);

        await().untilAsserted(() -> {
            verify(handler).handleAchievement(captor.capture());
            assertEquals(MENTOR_ID, captor.getValue().mentorId());
            assertEquals(MENTEE_ID, captor.getValue().menteeId());
        });
    }

    // RedisMessageListenerContainer is resilient to listener exceptions by design

    private void publishEvent() {
        redisTemplate.convertAndSend(
                mentorshipTopic,
                new MentorshipStartEvent(MENTOR_ID, MENTEE_ID, TIMESTAMP)
        );
    }
}