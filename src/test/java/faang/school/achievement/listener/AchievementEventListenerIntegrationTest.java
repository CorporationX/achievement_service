package faang.school.achievement.listener;

import faang.school.achievement.config.kafka.KafkaProperties;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.protobuf.generate.AchievementEventProto;
import faang.school.achievement.repository.AchievementProgressRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class AchievementEventListenerIntegrationTest {

    private static final int REDIS_PORT = 6379;

    @Autowired
    private KafkaTemplate<byte[], byte[]> kafkaTemplate;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Autowired
    private AchievementProgressRepository achievementProgressRepository;

    @Container
    private static final KafkaContainer KAFKA_CONTAINER =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @Container
    private static final GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>("redis:latest")
            .withExposedPorts(REDIS_PORT);

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
            .withInitScript("script/achievement_V001__initial.sql")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(REDIS_PORT));
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    @Test
    void testKafkaListenerReceivesEvent() {
        String topicName = kafkaProperties.getTopics().get("achievement").getName();
        long achievementId = 9L, userId = 1L;
        AchievementEventProto.AchievementEvent protoEvent = AchievementEventProto.AchievementEvent.newBuilder()
                .setUserId(userId)
                .setAchievementId(achievementId)
                .build();
        ProducerRecord<byte[], byte[]> event = new ProducerRecord<>(topicName, protoEvent.toByteArray());

        kafkaTemplate.send(event);

        await().atMost(30, SECONDS).untilAsserted(() -> {
            AchievementProgress result = achievementProgressRepository.findById(1L).orElse(null);
            assertNotNull(result);
            assertEquals(userId, result.getUserId());
            assertEquals(7, result.getCurrentPoints());
            assertEquals(1, result.getVersion());
        });
    }
}
