package faang.school.achievement.listener;

import faang.school.achievement.config.TestContainerConfig;
import faang.school.achievement.dto.event.ProjectCreateEvent;
import faang.school.achievement.event_validator.project_create.ProjectCreateEventValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = "project-create")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ProjectCreateEventListenerTest extends TestContainerConfig {
    @Autowired
    private KafkaTemplate<String, ProjectCreateEvent> projectCreateEventKafkaTemplate;

    @Autowired
    private Set<ProjectCreateEventValidator> projectCreateEventValidators;

    @Value("${spring.kafka.producer.project-create.topic}")
    private String projectCreateTopic;

    @Test
    void projectViewEventListener_ShouldSaveEvent() throws Exception {
        ProjectCreateEvent event = new ProjectCreateEvent(1L, 2L);

        for (int i = 0; i < 5; i++) {
            projectCreateEventKafkaTemplate.send(projectCreateTopic, event);
        }
        Thread.sleep(5000);

        for (ProjectCreateEventValidator projectCreateEventValidator : projectCreateEventValidators) {
            assertThat(projectCreateEventValidator.isValid(event.getAuthorId()))
                    .isEqualTo(true);
        }
    }
}