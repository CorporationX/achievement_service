package faang.school.achievement.listener;

import faang.school.achievement.dto.event.ProjectCreateEvent;
import faang.school.achievement.handler.project_create.ProjectCreateEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectCreateEventListener {
    private final List<ProjectCreateEventHandler> projectCreateEventHandlers;

    @KafkaListener(topics = "${spring.kafka.consumer.project-create.topic}",
    containerFactory = "kafkaProjectCreateListenerContainerFactory")
    public void projectViewEventListener(ProjectCreateEvent projectCreateEvent) {
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        for (ProjectCreateEventHandler projectCreateEventHandler : projectCreateEventHandlers) {
            completableFutures.add(projectCreateEventHandler.handle(projectCreateEvent));
        }

        for (CompletableFuture<Void> completableFuture : completableFutures) {
            completableFuture.exceptionally(ex -> {
                log.error("Error occurred while processing event", ex);
                throw new RuntimeException(ex);
            });
            completableFuture.join();
        }
    }
}