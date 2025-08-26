package faang.school.achievement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.property.kafka.KafkaProperty;
import faang.school.achievement.dto.event.CommentEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DirtiesContext
@Testcontainers
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
class CommentListenerIntTest {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaProperty kafkaProperty;
    @Autowired
    private ObjectMapper objectMapper;
    @SpyBean
    private CommentListener commentListener;
    @Captor
    private ArgumentCaptor<String> dataCaptor;
    @Container
    public static PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:13.3");

    @DynamicPropertySource
    static void propertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }

    @Test
    @DisplayName("Успешное чтение event из Kafka")
    void positive_consumerCommentEventReceived() throws JsonProcessingException {
        CommentEvent event = createCommentEvent();
        String expected = objectMapper.writeValueAsString(event);

        kafkaTemplate.send(kafkaProperty.topic().commentNew(), expected);

        verify(commentListener, timeout(5000).times(1)).consume(dataCaptor.capture());
        String actual = dataCaptor.getValue();
        assertEquals(expected, actual);
    }

    // --------------------------

    private CommentEvent createCommentEvent() {
        return CommentEvent.builder()
                .id(1)
                .postAuthorId(2)
                .commentAuthorId(3)
                .postId(4)
                .content("comment content")
                .build();
    }
}