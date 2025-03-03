package faang.school.achievement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.user.UserDto;
import faang.school.achievement.event.follower.FollowEvent;
import faang.school.achievement.handler.CelebrityAchievementHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.util.BaseContextTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FollowMessageListenerTest extends BaseContextTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private AchievementProgressRepository achievementProgressRepository;
    @Autowired
    private UserAchievementRepository userAchievementRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AchievementRepository achievementRepository;

    @Value("${spring.data.redis.channel.follow}")
    private String followChannel;

    private final long followerId = 2L;
    private final long followeeId = 1L;
    private final LocalDateTime createdAt = LocalDateTime.now();

    private FollowEvent followEvent = FollowEvent
            .builder()
            .followerId(followerId)
            .followeeId(followeeId)
            .createdAt(createdAt)
            .build();
    Achievement achievement;

    public FollowMessageListenerTest(@Autowired AchievementRepository achievementRepository) {
        achievement = achievementRepository.findByTitleIgnoreCase(
                CelebrityAchievementHandler.ACHIEVEMENT_NAME
        ).orElseThrow(() -> new AssertionError("Достижение не найдено"));
    }

    @Test
    void testOnMessage_UserWillNotGetAchievement() {
        stubGetUserById(followeeId);

        redisTemplate.convertAndSend(followChannel, followEvent);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        assertTrue(achievementProgressRepository
                                .findByUserIdAndAchievementId(followeeId, achievement.getId()).isPresent())
                );

        assertFalse(userAchievementRepository
                        .existsByUserIdAndAchievementId(followeeId, achievement.getId()),
                "Пользователю не должно присваиваться достижение");
    }

    private void stubGetUserById(long userId) {
        try {
            wireMock.stubFor(get("/users/api/v1/users/" + userId)
                    .willReturn(aResponse()
                            .withHeader("Content-Type", APPLICATION_JSON)
                            .withBody(objectMapper.writeValueAsString(
                                    UserDto.builder().id(userId).build()
                            ))
                    )
            );
        } catch (JsonProcessingException e) {
            throw new AssertionError("Ошибка сериализации объекта");
        }
    }

    @DynamicPropertySource
    static void setUserServicePort(DynamicPropertyRegistry registry) {
        registry.add("user-service.port", wireMock::getPort);
    }
}
