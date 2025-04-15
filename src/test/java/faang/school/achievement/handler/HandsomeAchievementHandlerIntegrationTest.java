package faang.school.achievement.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import faang.school.achievement.config.KafkaTestConfig;
import faang.school.achievement.config.kafka.KafkaProducerService;
import faang.school.achievement.config.kafka.properties.ProfilePicTopicProperties;
import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.exception.EventSerializationException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.util.BaseContextTest;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.TimeUnit;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@Import(KafkaTestConfig.class)
@Sql(scripts = {"/clear_users_data.sql", "/users_data.sql"}, executionPhase = BEFORE_TEST_METHOD)
class HandsomeAchievementHandlerIntegrationTest extends BaseContextTest {

    private static final long USER_ID = 1L;
    private static final String PROFILE_PIC_KEY = "user_profile_photos/123-test.jpg";
    private static final ProfilePicEvent PROFILE_PIC_EVENT = new ProfilePicEvent(USER_ID, PROFILE_PIC_KEY);

    @Value("${achievement.title.handsome}")
    private String handsomeAchievementTitle;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private ProfilePicTopicProperties profilePicTopicProperties;

    @Autowired
    private HandsomeAchievementHandler handsomeAchievementHandler;

    @AfterEach
    void cleanUp() {
        achievementProgressRepository.deleteAll();
        userAchievementRepository.deleteAll();
    }

    @Test
    void consumeSuccessfully() {
        kafkaProducerService.sendMessage(profilePicTopicProperties.name(), serializeProfilePicEvent());

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    Achievement handsomeAchievement = achievementRepositoryAdapter.getByTitle(handsomeAchievementTitle);
                    long handsomeAchievementId = handsomeAchievement.getId();

                    Long handsomeAchievementProgress = achievementService.getProgress(USER_ID,
                            handsomeAchievementId).getCurrentPoints();

                    Assertions.assertFalse(achievementService.hasAchievement(USER_ID, handsomeAchievementId));
                    Assertions.assertEquals(1L, handsomeAchievementProgress);
                });
    }

    @Test
    void getUserIdSuccessfully() {
        Assertions.assertEquals(USER_ID, handsomeAchievementHandler.getUserId(PROFILE_PIC_EVENT));
    }

    @Test
    void generateUniqueEventKey() {
        Assertions.assertEquals(PROFILE_PIC_KEY, handsomeAchievementHandler.generateUniqueEventKey(PROFILE_PIC_EVENT));
    }

    private String serializeProfilePicEvent() {
        try {
            return objectMapper.writeValueAsString(PROFILE_PIC_EVENT);
        } catch (JsonProcessingException e) {
            throw new EventSerializationException("An error occurred while serializing the event");
        }
    }
}
